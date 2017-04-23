package air.cleaner.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import air.cleaner.device.service.DeviceReceiveService;
import air.cleaner.model.CleanerStatus;
import air.cleaner.model.ResultMap;

@RequestMapping(value="/status")
@RestController
public class CleanerStatusController {
	@Autowired
	private DeviceReceiveService deviceReceiveService;
	public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
		this.deviceReceiveService = deviceReceiveService;
	}

	@RequestMapping(value="/device/{deviceID}")
	public ResultMap getCleanerStatus(@PathVariable("deviceID")String deviceString){
		ResultMap resultMap = new ResultMap();
		long deviceID = Long.parseLong(deviceString);
		CleanerStatus cleanerStatus = deviceReceiveService.getCleanerStatus(deviceID);
		if (cleanerStatus == null) {
			resultMap.setInfo("没有找到相应的设备");;
		}else{
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("status", cleanerStatus);
		}
		return resultMap;
	}
}
