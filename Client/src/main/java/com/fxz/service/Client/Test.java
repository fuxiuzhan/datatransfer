package com.fxz.service.Client;

public class Test {

	int id;
	String name;
	String addr;

	@Override
	public String toString() {
		return "Test [id=" + id + ", name=" + name + ", addr=" + addr + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}


}

