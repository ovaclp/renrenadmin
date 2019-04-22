package io.renren.modules.device.service.impl;

import io.renren.common.config.MinaClientConfig;
import io.renren.common.config.TCPConnector;
import io.renren.common.utils.*;
import io.renren.modules.device.entity.AgentinfoEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.service.AgentinfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.modules.device.dao.DeviceloginfoDao;
import io.renren.modules.device.entity.DeviceloginfoEntity;
import io.renren.modules.device.entity.DevlogcontentinfoEntity;
import io.renren.modules.device.service.DeviceloginfoService;
import io.renren.modules.device.service.DevlogcontentinfoService;

@Service("deviceloginfoService")
public class DeviceloginfoServiceImpl extends ServiceImpl<DeviceloginfoDao, DeviceloginfoEntity> implements DeviceloginfoService {



    /*@Autowired
    private MinaClientConfig minaClientConfig;*/
    @Autowired
    private DevlogcontentinfoService devlogcontentinfoService;
    public  int ee=10100000;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String devid = (String) params.get("devid");
        String dateStart = (String) params.get("dateStart");
        String dateEnd = (String) params.get("dateEnd");
        Page<DeviceloginfoEntity> page = null;
        if (StringUtils.isNotBlank(devid) && StringUtils.isNotBlank(dateStart) && StringUtils.isNotBlank(dateEnd)) {
            Timestamp timeStamep = null;
            Timestamp timeEnd = null;
            try {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateStart1 = sDateFormat.parse((String) params.get("dateStart"));
                Date dateEnd1 = sDateFormat.parse((String) params.get("dateEnd"));

                timeStamep = new Timestamp(dateStart1.getTime());

                timeEnd = new Timestamp(dateEnd1.getTime());

            } catch (ParseException px) {
                px.printStackTrace();

            }

            page = this.selectPage(
                    new Query<DeviceloginfoEntity>(params).getPage(),
                    new EntityWrapper<DeviceloginfoEntity>().like(StringUtils.isNotBlank(devid), "DevID", devid).between("LogTime", timeStamep, timeEnd)
            );
        }
        else {
            page = this.selectPage(
                    new Query<DeviceloginfoEntity>(params).getPage(),
                    new EntityWrapper<DeviceloginfoEntity>().like(StringUtils.isNotBlank(devid), "DevID", devid)
            );
        }

        //加入日志内容翻译
        for (DeviceloginfoEntity deviceloginfoEntity : page.getRecords()) {
            System.out.println("-----------------UUUUU---------------"+deviceloginfoEntity.getLogtime());
            DevlogcontentinfoEntity devlogcontentinfoEntity1 = new DevlogcontentinfoEntity();

            devlogcontentinfoEntity1.setLogcontent(deviceloginfoEntity.getLogcontent());
            EntityWrapper<DevlogcontentinfoEntity> wrapper = new EntityWrapper<>();
            wrapper.setEntity(devlogcontentinfoEntity1);

            DevlogcontentinfoEntity devlogcontentinfoEntity = devlogcontentinfoService.selectOne(wrapper);
            if (devlogcontentinfoEntity != null) {
                deviceloginfoEntity.setLogmeaning(devlogcontentinfoEntity.getLogmeaning());
            }
        }
        return new PageUtils(page);
    }


    public String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }


    public String dateHandlerFun(String data) {
        String datareturn = null;
        if (data.length() == 19) {

            datareturn = this.addZeroForNum(Integer.toHexString(Integer.parseInt(data.substring(0, 2))).toUpperCase(), 2) +
                    this.addZeroForNum(Integer.toHexString(Integer.parseInt(data.substring(2, 4))).toUpperCase(), 2) +
                    this.addZeroForNum(Integer.toHexString(Integer.parseInt(data.substring(5, 7))).toUpperCase(), 2) +
                    this.addZeroForNum(Integer.toHexString(Integer.parseInt(data.substring(8, 10))).toUpperCase(), 2) +
                    this.addZeroForNum(Integer.toHexString(Integer.parseInt(data.substring(11, 13))).toUpperCase(), 2) +
                    this.addZeroForNum(Integer.toHexString(Integer.parseInt(data.substring(14, 16))).toUpperCase(), 2) +
                    this.addZeroForNum(Integer.toHexString(Integer.parseInt(data.substring(17))).toUpperCase(), 2);


        }
        return datareturn;
    }

    @Override
    public List<DeviceloginfoEntity> queryDByDevid(String devid) {
        return baseMapper.queryDByDevidLog(devid);
    }

    @Override
    public List<DeviceinfoEntity> queryDevList() {
        return baseMapper.queryDevidList();
    }


    @Override
    public List<DeviceloginfoEntity> querylogByDate(Map<String, String> params) {

        String deviceid = params.get("devid");
        String deviceidHex = CodeUtils.strTo16(deviceid);
        String dateStart1 = this.dateHandlerFun(params.get("dateStart"));
        String dateEnd1 = this.dateHandlerFun(params.get("dateEnd"));


        MessageData message = new MessageData();
        ee=(ee+1);
        message.setMessageid("01A10000"+String.valueOf(ee));
        message.setLength("38000000");
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
        System.out.println("----"+agent_id+"---"+center_id);
        message.setSendid(agent_id);
        message.setDestinyid(center_id);

        message.setType("B4");
        message.setDevcieid(deviceidHex);

        String messHex = "0000000002000006" + dateStart1 + dateEnd1;
        //String messHex="0000000002000006141208010F1F001412081F00150A";
        System.out.println("消息部分--------" + messHex);
        message.setMessage(CodeUtils.hexStringToBytes(messHex));
        message.setMac("00000000000000000000000000000000");
        byte[] msg = {};
        //System.out.println("1231213"+message.getMessage());
        /*IoSession session = minaClientConfig.getSession(agentinfo.getAgentip());
        session.getConfig().setUseReadOperation(true);
        WriteFuture future=session.write(message);
        future.awaitUninterruptibly();
        if (future.isWritten()) {
            msg = (byte[]) session.read().awaitUninterruptibly().getMessage();
        }*/
        try{
            TCPConnector tcpConnector = new TCPConnector(agentinfo.getAgentip(),agentinfo.getAgentport());
            msg = (byte[])tcpConnector.sendMsg(message);
            tcpConnector.close();
        }catch (Exception e){

        }
        System.out.println("代理信息"+msg.length);
        ByteBuffer buffer = ByteBuffer.wrap(msg);

        byte[] devidbyte = new byte[32];
        buffer.get(devidbyte, 0, 32);
        byte[] startTime = new byte[7];
        buffer.get(startTime, 0, 7);
        byte[] endTime = new byte[7];
        buffer.get(endTime, 0, 7);
        byte[] countlog = new byte[4];
        buffer.get(countlog, 0, 4);
        int logCount = (countlog[2] & 0xff);
        System.out.println("-------------------" + logCount + "------------");
        if (logCount < 0) {
            return null;
        }
        List<DeviceloginfoEntity> listSet = new ArrayList<DeviceloginfoEntity>();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DeviceloginfoEntity entity = null;
        for (int i = 0; i < logCount; i++) {
            entity = new DeviceloginfoEntity();
            entity.setDevid(new String(devidbyte));
            byte[] log = new byte[13];
            buffer.get(log, 0, 13);

            entity.setLoglevel((int) log[1]);
            byte[] abc = new byte[4];
            abc[0] = log[2];
            abc[1] = log[3];
            abc[2] = log[4];
            abc[3] = log[5];
            String logcontent = CodeUtils.hexStr2Str(CodeUtils.bytesToHex(abc));
            entity.setLogcontent(logcontent);

            int a = (int) log[6];
            int b = (int) log[7];
            int c = (int) log[8];
            int d = (int) log[9];
            int e = (int) log[10];
            int f = (int) log[11];
            int g = (int) log[12];
            String data = this.addZeroForNum(Integer.toString(a), 2) + this.addZeroForNum(Integer.toString(b), 2) + "-" + this.addZeroForNum(Integer.toString(c), 2) + "-" + this.addZeroForNum(Integer.toString(d), 2) + " " +
                    this.addZeroForNum(Integer.toString(e), 2) + ":" + this.addZeroForNum(Integer.toString(f), 2) + ":" + this.addZeroForNum(Integer.toString(g), 2);
            try {
                Date date = sDateFormat.parse(data);
                Timestamp timeStamep = new Timestamp(date.getTime());
                entity.setLogtime(timeStamep);
            } catch (ParseException px) {

            }
            listSet.add(entity);
        }
        Timestamp timeStamep = null;
        Timestamp timeEnd = null;
        try {
            Date dateStart2 = sDateFormat.parse(params.get("dateStart"));
            Date dateEnd2 = sDateFormat.parse(params.get("dateEnd"));
            timeStamep = new Timestamp(dateStart2.getTime());
            timeEnd = new Timestamp(dateEnd2.getTime());
        } catch (ParseException px) {
            px.printStackTrace();

        }
        //先删除数据库里面的存在的记录，然后插入
        this.baseMapper.deleteDevidLogByDate(deviceid, timeStamep, timeEnd);
        this.insertBatch(listSet);
        return listSet;
    }

    public List<DeviceloginfoEntity> query_Devid(Map<String, Object> params) {
        String deviceid = (String) params.get("devid");
        Timestamp timeStamep = null;
        Timestamp timeEnd = null;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateStart = sDateFormat.parse((String) params.get("dateStart"));
            Date dateEnd = sDateFormat.parse((String) params.get("dateEnd"));
            timeStamep = new Timestamp(dateStart.getTime());
            timeEnd = new Timestamp(dateEnd.getTime());
        } catch (ParseException px) {
            px.printStackTrace();
        }
        return baseMapper.queryDevidLog(deviceid, timeStamep, timeEnd);
    }

}
