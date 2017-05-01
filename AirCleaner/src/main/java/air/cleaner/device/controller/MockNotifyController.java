package air.cleaner.device.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import air.cleaner.model.ResultMap;

@RestController
public class MockNotifyController {
	@RequestMapping(value="/assign/locate/{device}")
	public ResultMap getDeviceInfo(@PathVariable("device")long device){
		ResultMap resultMap = new ResultMap();
		resultMap.setStatus(ResultMap.STATUS_SUCCESS);
		resultMap.addContent("uid", device);
		resultMap.addContent("ip", "localhost");
		return resultMap;
	}
}
