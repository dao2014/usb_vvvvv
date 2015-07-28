package com.esp.socket.bean;

import java.util.Date;


public class Monitor {
		private String id;
		private String voltage;
		private String electricity;
		private String tc;
		private Date time;
		
		public Monitor(){}
		
		
		public Monitor(String id, String voltage, String electricity,String tc, Date time) {
			super();
			this.id = id;
			this.voltage = voltage;
			this.electricity = electricity;
			this.time = time;
			this.tc = tc;
		}
		
		
		public String getTc() {
			return tc;
		}


		public void setTc(String tc) {
			this.tc = tc;
		}


		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getVoltage() {
			return voltage;
		}
		public void setVoltage(String voltage) {
			this.voltage = voltage;
		}
		public String getElectricity() {
			return electricity;
		}
		public void setElectricity(String electricity) {
			this.electricity = electricity;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
		
		
		
}
