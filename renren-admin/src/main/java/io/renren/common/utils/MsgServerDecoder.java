package io.renren.common.utils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

import io.renren.common.config.AddressipRecord;
import io.renren.common.config.MinaClientConfig;
import io.renren.modules.device.entity.AgentinfoEntity;
import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.service.AgentinfoService;
import io.renren.modules.device.service.DevicedyninfoService;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.device.service.DeviceinfoService;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgServerDecoder extends CumulativeProtocolDecoder {


    private static Logger log = LoggerFactory.getLogger(MsgServerDecoder.class);

    Charset charset=Charset.forName("gb2312");
    //DevicedyninfoService devicedyninfoService=(DevicedyninfoService)SpringContextUtils.getBean("devicedyninfoService");


    public MsgServerDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {


        int flag=0;
        /**
         *获取返回值长度
         * 1	1	2	8	4	32	32	1	1	1	32	8  N	 16
         */
        if(in.limit()<139){
            return false;
        }

        byte[] temp=new byte[12];
        in.get(temp,0,12);
        //pdu长度
        byte[] length=new byte[4];
        in.get( length,0,4);

        String a=BytesUtil.bytesToHexString(length,0,4);
        int pudlen=Integer.parseInt(a.substring(0,2),16);

        if(pudlen<43){
            return false;
        }

        //读取中间数据
        byte[] temp1=new byte[67];
        in.get(temp1,0,67);
        //读取设备唯一id
        byte[] deviceid=new byte[32];
        in.get(deviceid,0,32);
        String dev_id=new String(deviceid);

        //获取数据内容vid

        //获取第一个vid
        byte[] vid=new byte[8];
        in.get(vid,0,8);
        String  vid1=CodeUtils.bytesToHex(vid);

        byte[] message=new byte[pudlen-42];
        in.get(message,0,pudlen-42);

        //System.out.println("vid  -------"+vid1);
        //System.out.println("data  -------"+message.length);

        //心跳信号
        if(vid1.equals("000000000F000005")){
            InitDataListener initDataListener=new InitDataListener();
            List<DevicedyninfonewEntity> initlist=initDataListener.getListnew();
            for (DevicedyninfonewEntity unit:initlist){
                //如果设备存在
                if(dev_id.equals(unit.getDevid())){
                    if("0".equals(unit.getDevstate())){
                       unit.setCountum(unit.getCountnum()+1);
                        System.out.println("心跳状态,在线,计数日志,设备"+dev_id+",计数值"+unit.getCountnum());
                    }
                    if("1".equals(unit.getDevstate())){
                        unit.setCountum(unit.getCountnum()+1);
                        Map<String, Object> map = new HashMap<>();
                        map.put("DevID", dev_id);
                        DevicedyninfonewService devicedyninfonewService=(DevicedyninfonewService)SpringContextUtils.getBean("devicedyninfonewService");

                        DevicedyninfoService devicedyninfoService=(DevicedyninfoService)SpringContextUtils.getBean("devicedyninfoService");
                        List<DevicedyninfonewEntity> list = devicedyninfonewService.selectByMap(map);
                        if(list!=null&&list.size()>0){
                            DevicedyninfonewEntity devicedyninfonewEntity=list.get(0);
                            devicedyninfonewEntity.setDevstate("0");
                            Date time=new Date();
                            Timestamp timestamp=new Timestamp(time.getTime());
                            devicedyninfonewEntity.setLastmodifytime(timestamp);
                            devicedyninfonewService.updateById(devicedyninfonewEntity);
                            unit.setDevstate("0");
                            unit.setCountum(1);
                            System.out.println("心跳状态,离线,计数日志,设备"+dev_id+",计数值"+unit.getCountnum());
                            DevicedyninfoEntity devicedyninfoEntity=CodeUtils.dynnewTransferDyn(devicedyninfonewEntity);
                            devicedyninfoEntity.setLastmodifytime(timestamp);
                            devicedyninfoService.insert(devicedyninfoEntity);
                        }
                    }
                    if("2".equals(unit.getDevstate())){
                       //毙杀计数置零
                       unit.setCountum(0);
                       System.out.println("心跳状态,毙杀,计数日志,设备"+dev_id+",计数值"+unit.getCountnum());
                    }

                }
            }

        }
        //全部基本信息
        if(vid1.equals("0000000000000001")){
           /* try {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getRemoteAddress();
                if (inetSocketAddress!=null){
                    String  ipinfo = inetSocketAddress.getHostName();
                    if (MinaClientConfig.flagMap.get(ipinfo)==null){
                        MinaClientConfig  minaClientConfig =(MinaClientConfig)SpringContextUtils.getBean("minaClientConfig");
                        AddressipRecord addressipRecord=new AddressipRecord();
                        addressipRecord.setAddress(ipinfo);
                        AgentinfoService agentinfoService = (AgentinfoService)SpringContextUtils.getBean("agentinfoService");
                        Map<String,Object> param = new HashMap<String,Object>();
                        param.put("AgentIP",ipinfo);
                        List<AgentinfoEntity> agentinfoEntities =  agentinfoService.selectByMap(param);
                        if (agentinfoEntities!=null && agentinfoEntities.size()>0){
                            addressipRecord.setPort(agentinfoEntities.get(0).getAgentport());
                            minaClientConfig.connect(addressipRecord);
                            MinaClientConfig.flagMap.put(ipinfo,"1");
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("Socket空了！！！！！！！！！！！！");
            }*/

            if(null==message&&message.length!=176){
                return false;
            }
            ByteBuffer buffer = ByteBuffer.wrap(message);
            DeviceinfoEntity deviceinfoEntity=new DeviceinfoEntity();

            byte[] devid=new byte[32];
            buffer.get(devid,0,32);
            deviceinfoEntity.setDeviceid(new String(devid));

            byte[] devname=new byte[40];
            buffer.get(devname,0,40);
            String dev_name=CodeUtils.removeTail0(CodeUtils.bytesToHex(devname));
            byte[] devv=CodeUtils.hexStringToBytes(dev_name);
            deviceinfoEntity.setDevicename(new String(devv, "gbk"));


            byte[] IssuerName=new byte[40];
            buffer.get(IssuerName,0,40);
            String issname=CodeUtils.removeTail0(CodeUtils.bytesToHex(IssuerName));
            byte[] isname=CodeUtils.hexStringToBytes(issname);
            deviceinfoEntity.setIssuername(new String(isname, "gbk"));

            byte[] deviceype=new byte[16];
            buffer.get(deviceype,0,16);
            deviceinfoEntity.setDevicetype(new String(deviceype));

            byte[] devSerial=new byte[16];
            buffer.get(devSerial,0,16);
            deviceinfoEntity.setDeviceserial(new String(devSerial));

            byte[] devVersion=new byte[4];
            buffer.get(devVersion,0,4);
            double devversion=(devVersion[0]&0xff)+(devVersion[1]&0xff) * Math.pow( 2,8)+(devVersion[2]&0xff)*Math.pow(2,16)+(devVersion[3]&0xff)*Math.pow(2,24);
            deviceinfoEntity.setDeviceversion((int)devversion);

            byte[] standradVersion=new byte[4];
            buffer.get(standradVersion,0,4);
            double standradversion=(standradVersion[0]&0xff)+(standradVersion[1]&0xff) * Math.pow( 2,8)+(standradVersion[2]&0xff)*Math.pow(2,16)+(standradVersion[3]&0xff)*Math.pow(2,24);
            deviceinfoEntity.setStandardversion((int)standradversion);

            byte[] devIP4=new byte[4];
            buffer.get(devIP4,0,4);
            String ipv4=(devIP4[0]&0xff)+"."+(devIP4[1]&0xff)+"."+(devIP4[2]&0xff)+"."+(devIP4[3]&0xff);
            deviceinfoEntity.setDeviceipv4(ipv4);

            byte[] asymAlgability1=new byte[4];
            buffer.get(asymAlgability1,0,4);
            double asymalgability1=(asymAlgability1[0]&0xff)+(asymAlgability1[1]&0xff) * Math.pow( 2,8)+(asymAlgability1[2]&0xff)*Math.pow(2,16)+(asymAlgability1[3]&0xff)*Math.pow(2,24);
            deviceinfoEntity.setAsymalgability0((int)asymalgability1);

            byte[] asymAlgability2=new byte[4];
            buffer.get(asymAlgability2,0,4);
            double asymalgability2=(asymAlgability2[0]&0xff)+(asymAlgability2[1]&0xff) * Math.pow( 2,8)+(asymAlgability2[2]&0xff)*Math.pow(2,16)+(asymAlgability2[3]&0xff)*Math.pow(2,24);
            deviceinfoEntity.setAsymalgability1((int)asymalgability2);

            byte[] symAlgability=new byte[4];
            buffer.get(symAlgability,0,4);
            double symalgability=(symAlgability[0]&0xff)+(symAlgability[1]&0xff) * Math.pow( 2,8)+(symAlgability[2]&0xff)*Math.pow(2,16)+(symAlgability[3]&0xff)*Math.pow(2,24);
            deviceinfoEntity.setSymalgability((int)symalgability);

            byte[] hashAlgability=new byte[4];
            buffer.get(hashAlgability,0,4);
            double hashalgability=(hashAlgability[0]&0xff)+(hashAlgability[1]&0xff) * Math.pow( 2,8)+(hashAlgability[2]&0xff)*Math.pow(2,16)+(hashAlgability[3]&0xff)*Math.pow(2,24);
            deviceinfoEntity.setHashalgability((int)hashalgability);

            byte[] buffersize=new byte[4];
            buffer.get(buffersize,0,4);
            deviceinfoEntity.setBuffersize(CodeUtils.bytesToInt(buffersize,0));


            Map<String,Object> map=new HashMap<>();
            map.put("DeviceID",dev_id);
            DeviceinfoService deviceinfoService=(DeviceinfoService)SpringContextUtils.getBean("deviceinfoService");
            List<DeviceinfoEntity> list=deviceinfoService.selectByMap(map);
            if(list!=null&&list.size()>0) {
                deviceinfoEntity.setRecnumber(list.get(0).getRecnumber());
                Date time = new Date();
                Timestamp timestamp = new Timestamp(time.getTime());
                deviceinfoEntity.setLastmodifytime(timestamp);
                deviceinfoService.updateById(deviceinfoEntity);
            }else{
                System.out.println("该设备未进行离线注册");
            }


            System.out.println("all base information  success！ ");


        }
        //全部状态信息
        if(vid1.equals("0000000000000005")){
            if(message.length!=45){
                return false;
            }
            ByteBuffer buffer = ByteBuffer.wrap(message);
            DevicedyninfonewEntity entity=new DevicedyninfonewEntity();

            byte[] devidbyte=new byte[32];
            buffer.get(devidbyte,0,32);
            entity.setDevid(new String(devidbyte));

            byte[] CryptoServiceState=new byte[1];
            buffer.get( CryptoServiceState,0,1);
            entity.setEncservicestate(String.valueOf(CryptoServiceState[0]&0xFF));

            byte[] KeyUpdateState=new byte[1];
            buffer.get( KeyUpdateState,0,1);
            entity.setKeyupdatestate(String.valueOf(KeyUpdateState[0]&0xFF));

            byte[] TunnelConnection=new byte[1];
            buffer.get( TunnelConnection,0,1);
            entity.setTunnelconnectivity(String.valueOf(TunnelConnection[0]&0xFF));

            byte[] IPAddr_State=new byte[1];
            buffer.get( IPAddr_State,0,1);
            entity.setDevipstate(String.valueOf(IPAddr_State[0]&0xFF));

            byte[] RNG_Check=new byte[1];
            buffer.get( RNG_Check,0,1);
            entity.setRngCheck(String.valueOf(RNG_Check[0]&0xFF));

            byte[] KernelProgCheck=new byte[1];
            buffer.get( KernelProgCheck,0,1);
            entity.setKernelprogcheck(String.valueOf(KernelProgCheck[0]&0xFF));

            byte[] AlgResCondition=new byte[3];
            buffer.get( AlgResCondition,0,3);
            entity.setAlgrescontion((AlgResCondition[2] & 0xFF)+
                    (AlgResCondition[1] & 0xFF)*100+
                    (AlgResCondition[0] & 0xFF)*10000);

            byte[] KeyResCondition=new byte[4];
            buffer.get( KeyResCondition,0,4);
            long a1=(KeyResCondition[3]&0xFF)+(KeyResCondition[2]&0xFF)*100+(KeyResCondition[1] & 0xFF)*10000+(KeyResCondition[0]&0xFF)*1000000;
            entity.setKeyrescondition(a1);
            entity.setDevstate("0");
            entity.setKeyversion("0");

            Date time=new Date();
            Timestamp timestamp=new Timestamp(time.getTime());
            entity.setLastmodifytime(timestamp);

            DevicedyninfoService devicedyninfoService=(DevicedyninfoService)SpringContextUtils.getBean("devicedyninfoService");
            DevicedyninfoEntity devicedyninfoEntity=CodeUtils.dynnewTransferDyn(entity);
            devicedyninfoService.insert(devicedyninfoEntity);

            Map<String,Object> map=new HashMap<>();
            map.put("DevID",dev_id);
            DevicedyninfonewService devicedyninfonewService=(DevicedyninfonewService)SpringContextUtils.getBean("devicedyninfonewService");
            List<DevicedyninfonewEntity> list=devicedyninfonewService.selectByMap(map);
            List<DevicedyninfonewEntity> initlist=new InitDataListener().getListnew();
            if(list!=null&&list.size()>0){
                DevicedyninfonewEntity devicedyninfonewEntity=list.get(0);
                //在线或者离线
                if(("0".equals(devicedyninfonewEntity.getDevstate()))||("1".equals(devicedyninfonewEntity.getDevstate()))){
                    entity.setRecnumber(devicedyninfonewEntity.getRecnumber());
                    devicedyninfonewService.updateById(entity);
                    //遍历查找内存表
                    for (int i=0;i<initlist.size();i++){
                        if(dev_id.equals(initlist.get(i).getDevid())){
                            if(("0".equals(initlist.get(i).getDevstate()))||("1".equals(initlist.get(i).getDevstate()))){
                                initlist.get(i).setCountum(initlist.get(i).getCountnum()+1);
                                System.out.println("全部状态,在线或者离线,计数日志,设备"+dev_id+",计数值"+initlist.get(i).getCountnum());
                                initlist.get(i).setDevstate("0");
                            }
                        }

                    }

                }else{
                    //毙杀
                    entity.setRecnumber(devicedyninfonewEntity.getRecnumber());
                    devicedyninfonewService.updateById(entity);
                    for (int i=0;i<initlist.size();i++){
                        if(dev_id.equals(initlist.get(i).getDevid())){
                            if(("0".equals(initlist.get(i).getDevstate()))||("1".equals(initlist.get(i).getDevstate()))){
                                initlist.get(i).setCountum(initlist.get(i).getCountnum()+1);
                                initlist.get(i).setDevstate("0");
                                System.out.println("全部状态,毙杀，计数日志,设备"+dev_id+",计数值"+initlist.get(i).getCountnum());
                            }
                        }

                    }

                }
            }else{
                devicedyninfonewService.insert(entity);
                entity.setCountum(1);
                initlist.add(entity);

            }

            System.out.println("all state information  success！ ");

        }


        if(vid1.equals("0000000002000005")){

        }
        if(vid1.equals("0000000003000005")){

        }
        if(vid1.equals("0000000004000005")){

        }
        if(vid1.equals("0000000008000005")){

        }
        if(vid1.equals("0000000009000005")){

        }
        if(vid1.equals("000000000A000005")){

        }
        if(vid1.equals("000000000B000005")){
            if(message.length!=3){
                return false;
            }
            DevicedyninfonewEntity entity=new DevicedyninfonewEntity();
            byte[] message1=message;
            int algrescondition =(message1[2] & 0xFF)+(message1[1] & 0xFF)*100+ (message1[0] & 0xFF)*10000;
            entity.setAlgrescontion(algrescondition);

            InitDataListener initDataListener=new InitDataListener();
            List<DevicedyninfonewEntity> initlist=initDataListener.getListnew();
            for (DevicedyninfonewEntity unit:initlist){
                if(dev_id.equals(unit.getDevid())){
                    if(("0".equals(unit.getDevstate()))||("1".equals(unit.getDevstate()))){
                        Map<String,Object> map=new HashMap<>();
                        map.put("DevID",dev_id);
                        DevicedyninfonewService devicedyninfonewService=(DevicedyninfonewService)SpringContextUtils.getBean("devicedyninfonewService");
                        DevicedyninfoService devicedyninfoService=(DevicedyninfoService)SpringContextUtils.getBean("devicedyninfoService");
                        List<DevicedyninfonewEntity> list=devicedyninfonewService.selectByMap(map);
                        if(list!=null&&list.size()>0){
                            DevicedyninfonewEntity devicedyninfonewEntity=list.get(0);
                            devicedyninfonewEntity.setAlgrescontion(algrescondition);
                            devicedyninfonewEntity.setDevstate("0");
                            Date time=new Date();
                            Timestamp timestamp=new Timestamp(time.getTime());
                            devicedyninfonewEntity.setLastmodifytime(timestamp);

                            devicedyninfonewService.updateById(devicedyninfonewEntity);
                            DevicedyninfoEntity devicedyninfoEntity=CodeUtils.dynnewTransferDyn(devicedyninfonewEntity);
                            devicedyninfoService.insert(devicedyninfoEntity);
                        }

                        unit.setCountum(unit.getCountnum()+1);
                        System.out.println("部分状态,在线或离线,计数日志,设备"+unit.getDevid()+",计数值"+unit.getCountnum());

                        unit.setDevstate("0");
                    }
                    if("2".equals(unit.getDevstate())){
                        unit.setCountum(0);
                        System.out.println("部分状态,毙杀,计数日志,设备"+unit.getDevid()+",计数值"+unit.getCountnum());

                    }
                }else{
                    //发送部分状态信息，内存表却无,,不存在
                }
            }
            System.out.println(" A  state information  success ");


        }

        return true;
    }
}
