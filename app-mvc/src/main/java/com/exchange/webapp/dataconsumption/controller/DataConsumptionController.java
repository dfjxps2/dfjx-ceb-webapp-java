package com.exchange.webapp.dataconsumption.controller;


import com.exchange.webapp.dataconsumption.bean.DataConsumption;
import com.exchange.webapp.dataconsumption.service.DataConsumptionService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据消费Controller
 */
@Controller
@RequestMapping("/consumption")
public class DataConsumptionController {

    @Autowired
    private DataConsumptionService dataConsumptionService;




    //数据消费管理列表
    @RequestMapping("/dataconsumptionList")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String dataconsumptionList(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam("yyxm")String yyxm,
            @RequestParam("gjz")String gjz
            ){
        PageResult pageResult = null;
        String jsonResult = "";
        try{
            pageResult = dataConsumptionService.dataconsumptionList(currPage,pageSize,yyxm,gjz);
        }catch(Exception e){
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "查询数据消费管理列表有误", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "查询数据消费管理列表成功", null, pageResult);
    }







//查看
    @RequestMapping("/dataconsumptionselect")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String dataconsumptionselect(
            @RequestParam("user_id") String user_id){
        List<DataConsumption> contactPageDatas;
        String jsonResult = "";
        try{
            contactPageDatas = dataConsumptionService.dataconsumptionselect(user_id);
        }catch(Exception e){
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "数据消费查看失败", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "数据消费查看成功", null, contactPageDatas);
    }


    //新增
    @RequestMapping("/insertdataconsumption")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertdataconsumption(
            @RequestParam("user_name") String user_name,
            @RequestParam("mobile_phone")String mobile_phone,
            @RequestParam("email")String email){
        String jsonResult = "";

        if(!user_name.isEmpty() && !mobile_phone.isEmpty() && !email.isEmpty()){
            try{
                dataConsumptionService.insertdataconsumption(user_name,mobile_phone,email);
            }catch(Exception e){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增数据消费失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增数据消费成功", null, "success");
    }


    //修改
    @RequestMapping("/updatedataconsumption")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatedataconsumption(
            @RequestParam("user_id") String user_id,
            @RequestParam("user_name") String user_name,
            @RequestParam("mobile_phone")String mobile_phone,
            @RequestParam("email")String email){
        String jsonResult = "";

        if(!user_name.isEmpty() && !mobile_phone.isEmpty() && !email.isEmpty()){
            try{
                dataConsumptionService.updatedataconsumption(user_id,user_name,mobile_phone,email);
            }catch(Exception e){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "数据消费修改失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "数据消费修改成功", null, "success");
    }



    //删除
    @RequestMapping("/delconsumption")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String delpageContact(
            @RequestParam("user_id") String user_id){
        String jsonResult = "";

        try{
            dataConsumptionService.delpageContact(user_id);
        }catch(Exception e){
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除成功", null, "error");
    }

}