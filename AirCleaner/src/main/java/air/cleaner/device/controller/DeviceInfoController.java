package air.cleaner.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import air.cleaner.device.service.DeviceControlService;
import air.cleaner.device.service.DeviceReceiveService;
import air.cleaner.model.DeviceInfo;
import air.cleaner.model.ResultMap;
@RequestMapping(value="/info")
@RestController
public class DeviceInfoController {
	@Autowired
	private DeviceControlService deviceControlService;
	public void setDeviceControlService(DeviceControlService deviceControlService) {
		this.deviceControlService = deviceControlService;
	}
	@Autowired
	private DeviceReceiveService deviceReceiveService;
	public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
		this.deviceReceiveService = deviceReceiveService;
	}

	@RequestMapping(value="/device/{deviceID}")
	public ResultMap getDeviceInfo(@PathVariable("deviceID")long deviceID){
		ResultMap resultMap = new ResultMap();
		DeviceInfo deviceInfo = deviceReceiveService.getDeviceInfo(deviceID);
		if (deviceInfo == null) {
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			resultMap.setInfo("没有找到相应的设备");
		}else {
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("info", deviceInfo);
		}
		return resultMap;
	}
	
}
