package air.cleaner.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import air.cleaner.annotation.AQIData;
import air.cleaner.annotation.Command;
import air.cleaner.utils.Constant;
import air.cleaner.utils.MethodUtil;

public class CleanerStatus implements Serializable{
	
	private static final long serialVersionUID = 3050484443988023484L;
	
	private long deviceID;
	@AQIData(start=0x00,length=2,name=Constant.PM25)
	private int pm25;
	@AQIData(start=0x02,length=1,name=Constant.TEMPERATURE)
	private int temperature;
	@AQIData(start=0x03,length=1,name=Constant.HUMIDITY)
	private int humidity;
	@AQIData(start=0x04,length=2,name=Constant.HCHO)
	private int HCHO;
	@AQIData(start=0x06,length=2,name=Constant.CO2)
	private int CO2;
	
	@Command(id=0x06,length=2,name=Constant.VELOCITY)
	@AQIData(start=0x08,length=2,name=Constant.VELOCITY)
	private int velocity;
	
	@Command(id=0x04,length=1,name=Constant.POWER)
	@AQIData(start=0x0A,length=1,name=Constant.POWER)
	private int power;
	
	@Command(id=0x05,length=1,name=Constant.WORKMODE)
	@AQIData(start=0x0B,length=1,name=Constant.WORKMODE)
	private int workMode;
	
	@Command(id=0x07,length=1,name=Constant.UV)
	@AQIData(start=0x0C,length=1,name=Constant.UV)
	private int UV;
	
	@Command(id=0x08,length=1,name=Constant.HEAT)
	@AQIData(start=0x0D,length=1,name=Constant.HEAT)
	private int heat;

	public long getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(long deviceID) {
		this.deviceID = deviceID;
	}

	public int getPm25() {
		return pm25;
	}

	public void setPm25(int pm25) {
		this.pm25 = pm25;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getHCHO() {
		return HCHO;
	}

	public void setHCHO(int hCHO) {
		HCHO = hCHO;
	}

	public int getCO2() {
		return CO2;
	}

	public void setCO2(int cO2) {
		CO2 = cO2;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public int getWorkMode() {
		return workMode;
	}

	public void setWorkMode(int workMode) {
		this.workMode = workMode;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getUV() {
		return UV;
	}

	public void setUV(int uV) {
		UV = uV;
	}

	public int getHeat() {
		return heat;
	}

	public void setHeat(int heat) {
		this.heat = heat;
	}

	public static CleanerStatus generateDefault(){
		CleanerStatus cleanerStatus = new CleanerStatus();
		Field[] fields = cleanerStatus.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(AQIData.class)){
				AQIData aqiData = field.getAnnotation(AQIData.class);
				//identify field name
				String name = aqiData.name();
				String type = field.getGenericType().getTypeName();
				String setName = MethodUtil.setFieldGetMethod(name);
				
				try {
					if(type.equals("int")){
						Method setMethod = cleanerStatus.getClass().getDeclaredMethod(setName, int.class);
						setMethod.invoke(cleanerStatus, -1);
					}else if(type.equals("long")){
						Method setMethod = cleanerStatus.getClass().getDeclaredMethod(setName, long.class);
						setMethod.invoke(cleanerStatus, -1L);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		cleanerStatus.setTemperature(2000);
		return cleanerStatus;
	}

}
