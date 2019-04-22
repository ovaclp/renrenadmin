package io.renren.modules.device.service.impl;


import io.renren.common.config.MinaClientConfig;
import io.renren.common.config.TCPConnector;
import io.renren.common.utils.*;
import io.renren.modules.device.entity.*;
import io.renren.modules.device.service.AgentinfoService;
import io.renren.modules.device.service.DevicedyninfoService;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.modules.device.dao.DeviceinfoDao;
import io.renren.modules.device.service.DeviceinfoService;


@Service("deviceinfoService")
public class DeviceinfoServiceImpl extends ServiceImpl<DeviceinfoDao, DeviceinfoEntity> implements DeviceinfoService {


//    @Autowired
//    private MinaClientConfig minaClientConfig;
    @Autowired
    private DevicedyninfonewService devicedyninfonewService;

    @Autowired
    private DevicedyninfoService devicedyninfoService;

    @Autowired
    private SysDeptService sysDeptService;


    public int localCount=10000001;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DeviceinfoEntity> page = this.selectPage(
                new Query<DeviceinfoEntity>(params).getPage(),
                new EntityWrapper<DeviceinfoEntity>()
        );

        return new PageUtils(page);
    }



    @Override
    public List<DeviceEntity> queryDevice() {
        List<SysDeptEntity> sysDeptEntities=sysDeptService.selectList(null);

        List<DeviceEntity> deviceEntities=new ArrayList<>();
        for(SysDeptEntity sysDeptEntity:sysDeptEntities){
            DeviceEntity deviceEntity=new DeviceEntity();
            deviceEntity.setParentId(sysDeptEntity.getParentId());
            deviceEntity.setDeptId(sysDeptEntity.getDeptId());
            deviceEntity.setName(sysDeptEntity.getName());
            deviceEntities.add(deviceEntity);
            Map<String, Object> map=new HashMap<>();
            map.put("sysnumber",sysDeptEntity.getDeptId());//
            List<DeviceinfoEntity> list=this.selectByMap(map);//取到监控中心下的所有设备基本信息

            for(DeviceinfoEntity deviceinfoEntity:list) {
                map=new HashMap<>();
                map.put("devid",deviceinfoEntity.getDeviceid());
                List<DevicedyninfonewEntity> devicedyninfonewEntities=devicedyninfonewService.selectByMap(map);
                DeviceEntity entity = new DeviceEntity();
                if(devicedyninfonewEntities.size()!=0) {
                    DevicedyninfonewEntity devicedyninfonewEntity =devicedyninfonewEntities.get(0);
                    entity.setDevstate(devicedyninfonewEntity.getDevstate());
                    entity.setNotice(devicedyninfonewEntity.getNotice());
                }

                entity.setParentId(sysDeptEntity.getDeptId());
                //entity.setName(sysDeptEntity.getName());
                entity.setDevID(deviceinfoEntity.getDeviceid());
                //entity.setDeptId(deviceinfoEntity.getSysnumber());
                entity.setDeviceName(deviceinfoEntity.getDevicename());
                entity.setDevName(deviceinfoEntity.getDevname());
                deviceEntities.add(entity);
            }
        }
        return deviceEntities;
    }



    /**
     * 从代理查询设备的基本信息
     * @param deviceid
     * @return
     */
    @Override
    public DeviceinfoEntity queryallInfoById(String deviceid) throws UnsupportedEncodingException {
        String Hex_devid=null;
        if(deviceid.length()==32){
            Hex_devid=CodeUtils.strTo16(deviceid);
        }else{
            return  null;
        }
        MessageData message=new MessageData();

        localCount=(localCount+1);
        message.setMessageid("01A10000"+String.valueOf(localCount));
        message.setLength("2A000000");

        AgentinfoService agentinfoService= (AgentinfoService) SpringContextUtils.getBean("agentinfoService");

        List<AgentinfoEntity> list=agentinfoService.agent_id(deviceid);
        AgentinfoEntity agentinfo=new AgentinfoEntity();
        if(list!=null&&list.size()>0){
            agentinfo=list.get(0);
        }
        String agentid=agentinfo.getAgentid();
        String centerid=agentinfo.getCenterid();

        String agent_id=CodeUtils.strTo16(agentid);
        String center_id=CodeUtils.strTo16(centerid);
        message.setSendid(agent_id);
        message.setDestinyid(center_id);
        message.setDevcieid(Hex_devid);
        message.setType("B0");
        message.setMessage(CodeUtils.hexStringToBytes("0000000000000001"));
        message.setMac("00000000000000000000000000000000");
        byte[] msg={};
        try{
            //long startTime=System.currentTimeMillis();   //获取开始时间

            TCPConnector tcpConnector = new TCPConnector(agentinfo.getAgentip(),agentinfo.getAgentport());
            long endTime1=System.currentTimeMillis(); //获取结束时间
            msg = (byte[])tcpConnector.sendMsg(message);
            //msg = (byte[])readF.getMessage();
            //long endTime2=System.currentTimeMillis(); //获取结束时间
            tcpConnector.close();
            //long endTime3=System.currentTimeMillis(); //获取结束时间
            //System.out.println("时间"+(endTime1-startTime)+"ms,"+"时间"+(endTime2-endTime1)+"ms,"+"时间"+(endTime3-endTime2)+"ms,");
        }catch (Exception e){

        }
        /*IoSession session = minaClientConfig.getSession(agentinfo.getAgentip());
        session.getConfig().setUseReadOperation(true);
        if(session.isConnected()){
            WriteFuture future=session.write(message);
            future.awaitUninterruptibly();

            if (future.isWritten()) {
                msg = (byte[]) minaClientConfig.getSession(agentinfo.getAgentip()).read().awaitUninterruptibly().getMessage();
            }
        }else{
            return null;
        }*/

        //System.out.println("代理信息"+msg.length);
        ByteBuffer buffer = ByteBuffer.wrap(msg);
        DeviceinfoEntity entity=new DeviceinfoEntity();

        byte[] devidbyte=new byte[32];
        buffer.get(devidbyte,0,32);
        entity.setDeviceid(new String(devidbyte));

        byte[] devname=new byte[40];
        buffer.get(devname,0,40);
        String dev_name=CodeUtils.removeTail0(CodeUtils.bytesToHex(devname));
        //String dev_name= CodeUtils.bytesToHex(devname);
        byte[] devv=CodeUtils.hexStringToBytes(dev_name);
        entity.setDevicename(new String(devv,"gbk"));

        byte[] issuername=new byte[40];
        buffer.get(issuername,0,40);
        String issname=CodeUtils.removeTail0(CodeUtils.bytesToHex(devname));
        //String issname=CodeUtils.bytesToHex(devname);

        byte[] isname=CodeUtils.hexStringToBytes(issname);
        entity.setIssuername(new String(isname,"gbk"));

        byte[] devicetype=new byte[16];
        buffer.get(devicetype,0,16);
        entity.setDevicetype(new String(devicetype));

        byte[] deviceserial=new byte[16];
        buffer.get(deviceserial,0,16);
        entity.setDeviceserial(new String(deviceserial));

        byte[] deversion=new byte[4];
        buffer.get(deversion,0,4);
        String version= BytesUtil.bytesToHexString(deversion,0, deversion.length);
        String devversion=version.substring(6)+version.substring(4,6)+version.substring(2,4)+version.substring(0,2);
        entity.setDeviceversion(Integer.parseInt(devversion,16));

        byte[] standardversion=new byte[4];
        buffer.get(standardversion,0,4);
        String standversion= BytesUtil.bytesToHexString(standardversion,0, standardversion.length);
        String standard_version=standversion.substring(6)+standversion.substring(4,6)+standversion.substring(2,4)+standversion.substring(0,2);
        entity.setStandardversion(Integer.parseInt(standard_version,16));

        byte[] ipv4=new byte[4];
        buffer.get(ipv4,0,4);
        String ipadress= BytesUtil.bytesToHexString(ipv4,0, ipv4.length);
        String ip4=Integer.parseInt(ipadress.substring(0,2),16)+"."+Integer.parseInt(ipadress.substring(2,4),16)+"."+Integer.parseInt(ipadress.substring(4,6),16)+"."+Integer.parseInt(ipadress.substring(6),16);
        entity.setDeviceipv4(ip4);

        byte[] asymalgability0=new byte[4];
        buffer.get(asymalgability0,0,4);
        entity.setAsymalgability0(CodeUtils.bytesToInt(asymalgability0,0));

        byte[] asymalgability1=new byte[4];
        buffer.get(asymalgability1,0,4);
        entity.setAsymalgability1(CodeUtils.bytesToInt(asymalgability1,0));

        byte[] symalgability=new byte[4];
        buffer.get(symalgability,0,4);
        entity.setSymalgability(CodeUtils.bytesToInt(symalgability,0));

        byte[] hashalgability=new byte[4];
        buffer.get(hashalgability,0,4);
        entity.setHashalgability(CodeUtils.bytesToInt(hashalgability,0));

        byte[] buffersize=new byte[4];
        buffer.get(buffersize,0,4);
        entity.setBuffersize(CodeUtils.bytesToInt(buffersize,0));

        return entity;


    }


    /*
     * 查询设备是否本级监控中心
     */
    public String  is_Current(String  devid){
        return baseMapper.isCurrent(devid);
    }

    public List<Map<String,Object>>  queryDev_ipid(String  devid){
        return baseMapper.queryDevipid(devid);
    }

    public String  devid_sysNumber(String  devid){
        return baseMapper.devidSysNumber(devid);
    }

    public int killStrategy(String devid){
        return baseMapper.killStrategy(devid);
    }

}
