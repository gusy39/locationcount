package com.ecmoho.data;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meidejing on 2016/8/12.
 */
@Component
public class LocationCount {
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    public void updatelocationCount(){
        String activityResourceSelectSql="select BrandID,Year,Month, ResourceName_Channel,Num from Input_Dat_ActivityResource";
        String locationPlanSelectSql="select year(Start_Time) as year,month(Start_Time) as month,c.BrandID,location_Plan from Input_Dat_Activity_Order a join Bas_Inf_CostCenter b on a.CostcenterId=b.CostCenterID\n" +
                "join Bas_Inf_Brand c on b.BrandID=c.BrandID";
        List<Map<String, Object>> activityResourceList = jdbcTemplate.queryForList(activityResourceSelectSql);
        Map<String,Integer> activityResourceMap=new HashMap<String,Integer>();
        for (Map<String, Object> map:activityResourceList){
            String year=map.get("year").toString();
            String month=map.get("month").toString();
            String BrandID=map.get("BrandId").toString();
            String location_Plan=map.get("ResourceName_Channel").toString();
            activityResourceMap.put(year+"_"+month+"_"+BrandID+"_"+location_Plan,0);
        }
        List<Map<String, Object>> locationPlanList = jdbcTemplate.queryForList(locationPlanSelectSql);
        for (Map<String, Object> map:locationPlanList){
            String year=map.get("year").toString();
            String month=map.get("month").toString();
            String BrandID=map.get("BrandId").toString();
            String location_Plan=map.get("location_Plan")==null?"":map.get("location_Plan").toString();
            String[] location_PlanArr = location_Plan.split(";");
            for (String location_plan:location_PlanArr){
                if(activityResourceMap.containsKey(year+"_"+month+"_"+BrandID+"_"+location_plan)){
                    int i=activityResourceMap.get(year+"_"+month+"_"+BrandID+"_"+location_plan)+1;
                    activityResourceMap.put(year+"_"+month+"_"+BrandID+"_"+location_plan,i);
                }
            }
        }
        List<String> sqlList=new ArrayList<String>();
        for (Map.Entry<String,Integer> e :activityResourceMap.entrySet()){
            String[] mosaicArr=e.getKey().split("_");
            String activityResourceUpdateSql= "update Input_Dat_ActivityResource set Num= "+e.getValue()+" " +
                    " where Year = "+mosaicArr[0]+" and Month = "+mosaicArr[1]+" AND BrandID = "+mosaicArr[2]+" AND ResourceName_Channel = '"+mosaicArr[3]+"' ";
            sqlList.add(activityResourceUpdateSql);
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

}
