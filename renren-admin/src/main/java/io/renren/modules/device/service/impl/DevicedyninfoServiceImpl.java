package io.renren.modules.device.service.impl;


import io.renren.common.config.AddressipRecord;
import io.renren.common.config.MinaClientConfig;
import io.renren.common.config.MinaClientConfignew;
import io.renren.common.config.TCPConnector;
import io.renren.common.utils.*;
import io.renren.modules.device.entity.AgentinfoEntity;
import io.renren.modules.device.entity.DevicedyninfonewEntity;

import io.renren.modules.device.service.AgentinfoService;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.modules.device.dao.DevicedyninfoDao;
import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.service.DevicedyninfoService;


@Service("devicedyninfoService")
public class DevicedyninfoServiceImpl extends ServiceImpl<DevicedyninfoDao, DevicedyninfoEntity> implements DevicedyninfoService {

/*
    @Autowired
    private MinaClientConfig minaClientConfig;*/

    @Override
    public PageUtils queryPage(Map<String, Object> params) {


        Page<DevicedyninfoEntity> page=this.selectPage(
                new Query<DevicedyninfoEntity>(params).getPage(),
                new EntityWrapper<DevicedyninfoEntity>().orderBy("DevID").orderBy("LastModifyTime",false));

        return new PageUtils(page);

    }

    /**
     * 从代理查询设备动态信息
     * @param deviceid  设备devid
     * @return
     */

    @Override
    public DevicedyninfoEntity queryallInfoById(String deviceid) {
        //找本级监控中心id(32)  和设备所在代理id(32)


        MessageData message=new MessageData();
        int a=11000000;
        a=(a+1);
        message.setMessageid("02000000"+String.valueOf(a));
       // message.setMessageid("02000000"+00000000");
        message.setLength("2A000000");
        AgentinfoService agentinfoService= (AgentinfoService) SpringContextUtils.getBean("agentinfoService");
        List<AgentinfoEntity> list=agentinfoService.agent_id(deviceid);

        String agentid=list.get(0).getAgentid();
        String centerid=list.get(0).getCenterid();
        String agent_id=CodeUtils.strTo16(agentid);
        String center_id=CodeUtils.strTo16(centerid);
        message.setSendid(agent_id);
        message.setDestinyid(center_id);

        String devid=CodeUtils.strTo16(deviceid);
        message.setDevcieid(devid);
        message.setType("B0");
        message.setMessage(CodeUtils.hexStringToBytes("0000000000000005"));
        message.setMac("00000000000000000000000000000000");
        byte[] msg={};

        AgentinfoEntity agentinfo=new AgentinfoEntity();
        if(list!=null&&list.size()>0){
            agentinfo=list.get(0);
        }
        /*IoSession session = minaClientConfig.getSession(agentinfo.getAgentip());
        session.getConfig().setUseReadOperation(true);
        WriteFuture future=session.write(message);
        future.awaitUninterruptibly();

        if (future.isWritten()) {
            msg = (byte[]) minaClientConfig.getSession(agentinfo.getAgentip()).read().awaitUninterruptibly().getMessage();
        }*/
        try{
            TCPConnector tcpConnector = new TCPConnector(agentinfo.getAgentip(),agentinfo.getAgentport());
            msg = (byte[])tcpConnector.sendMsg(message);
            tcpConnector.close();
        }catch (Exception e){

        }
        System.out.println("代理信息"+msg.length);
        ByteBuffer buffer = ByteBuffer.wrap(msg);

        DevicedyninfoEntity entity=new DevicedyninfoEntity();

        byte[] devidbyte=new byte[32];
         buffer.get(devidbyte,0,32);
        entity.setDevid(new String(devidbyte));
        //密码服务状态
        byte[] CryptoServiceState=new byte[1];
        buffer.get( CryptoServiceState,0,1);
        //entity.setEncservicestate(new String(CryptoServiceState));
        entity.setEncservicestate(String.valueOf(CryptoServiceState[0]&0xFF));
        //密钥更新状态
        byte[] KeyUpdateState=new byte[1];
        buffer.get( KeyUpdateState,0,1);
        //entity.setKeyupdatestate(new String(KeyUpdateState));
        entity.setKeyupdatestate(String.valueOf(KeyUpdateState[0]&0xFF));
        //隧道连通性
        byte[] TunnelConnection=new byte[1];
        buffer.get( TunnelConnection,0,1);
        //entity.setTunnelconnectivity(new String(TunnelConnection));
        entity.setTunnelconnectivity(String.valueOf(TunnelConnection[0]&0xFF));

        //ip地址是否变化
        byte[] IPAddr_State=new byte[1];
        buffer.get( IPAddr_State,0,1);
        //entity.setDevipstate(new String(IPAddr_State));
        entity.setDevipstate(String.valueOf(IPAddr_State[0]&0xFF));
        //随机数发生器校验状态
        byte[] RNG_Check=new byte[1];
        buffer.get( RNG_Check,0,1);
        //entity.setRngCheck(new String(RNG_Check));
        entity.setRngCheck(String.valueOf(RNG_Check[0]&0xFF));
        //关键程序校验
        byte[] KernelProgCheck=new byte[1];
        buffer.get( KernelProgCheck,0,1);
        //entity.setKernelprogcheck(new String(KernelProgCheck));
        entity.setKernelprogcheck(String.valueOf(KernelProgCheck[0]&0xFF));
        //密码算法使用率
        byte[] AlgResCondition=new byte[3];
        buffer.get( AlgResCondition,0,3);
        entity.setAlgrescontion((AlgResCondition[2] & 0xFF)+
                (AlgResCondition[1] & 0xFF)*100+
                (AlgResCondition[0] & 0xFF)*10000);
        //密钥资源利用率
        byte[] KeyResCondition=new byte[4];
        buffer.get( KeyResCondition,0,4);
        long a5=(KeyResCondition[3]&0xFF)+(KeyResCondition[2]&0xFF)*100+(KeyResCondition[1] & 0xFF)*10000+(KeyResCondition[0]&0xFF)*1000000;
        entity.setKeyrescondition(a5);

        entity.setKeyversion("0");
        Date time=new Date();
        Timestamp timestamp=new Timestamp(time.getTime());
        entity.setLastmodifytime(timestamp);

        return entity;
    }

    /**
     * 设备毙杀
     * @param deviceid
     * @return
     */
    public Boolean KilldevById(String deviceid){
        MessageData message=new MessageData();
        int a=11100000;
        a=(a+1);
        message.setMessageid("04000000"+String.valueOf(a));

       // message.setMessageid("0400000000000000");
        message.setLength("2A000000");
        AgentinfoService agentinfoService= (AgentinfoService) SpringContextUtils.getBean("agentinfoService");
        List<AgentinfoEntity> list=agentinfoService.agent_id(deviceid);
        String agentid=list.get(0).getAgentid();

        String centerid=list.get(0).getCenterid();
        String  port=agentid+":"+list.get(0).getAgentport();

        String agent_id=CodeUtils.strTo16(agentid);
        String center_id=CodeUtils.strTo16(centerid);
        message.setSendid(agent_id);
        message.setDestinyid(center_id);

        String devid=CodeUtils.strTo16(deviceid);
        message.setDevcieid(devid);

        message.setType("B3");
        message.setMessage(CodeUtils.hexStringToBytes("0000000001000004"));
        message.setMac("00000000000000000000000000000000");
        byte[] msg={};
        AgentinfoEntity agentinfo=new AgentinfoEntity();
        if(list!=null&&list.size()>0){
            agentinfo=list.get(0);
        }
       /* IoSession session = minaClientConfig.getSession(agentinfo.getAgentip());
        session.getConfig().setUseReadOperation(true);
        WriteFuture future=session.write(message);
        future.awaitUninterruptibly();

        if (future.isWritten()) {
            msg = (byte[]) minaClientConfig.getSession(agentinfo.getAgentip()).read().awaitUninterruptibly().getMessage();
        }*/
        try{
            TCPConnector tcpConnector = new TCPConnector(agentinfo.getAgentip(),agentinfo.getAgentport());
            msg = (byte[])tcpConnector.sendMsg(message);
            tcpConnector.close();
        }catch (Exception e){

        }
        System.out.println("代理信息"+msg.length);
        //ByteBuffer buffer = ByteBuffer.wrap(msg);

        String viddd=CodeUtils.bytesToHex(msg);
        System.out.println("------------"+viddd);
        if(viddd.equals("0000000001000004")){
            return true;
        }else{
            return false;
        }






    }


    /*@Override
    public ArrayList<DevicedyninfonewEntity> queryDeviceBydevid(){
        return baseMapper.queryDeviceBydevid();
    }*/



    public DevicedyninfoEntity devState(String devid){
        List<DevicedyninfoEntity> list= baseMapper.devidState(devid);
        return list.get(0);

    }
    public  List<DevicedyninfoEntity> query_DevdynPage(){
        return baseMapper.queryDevdynPage();
    }



}
