package com.fxz.service.DataTransferServer.Auth.config;

public class AuthConfig {
	private String symEncrypt = "none";
	private String nonSymEncryt = "none";
	private String authdigest = "md5";
	private String messageDigest = "none";
	private int timeOut = 60;
	private String ip;
	private int port;
	private int nonsymBlockSize = 512;
	private String private_key = null;
	private String public_key = null;
	private String remote_public_key = null;
	private boolean server = false;

	public String getMessageDigest() {
		return messageDigest;
	}

	public void setMessageDigest(String messageDigest) {
		this.messageDigest = messageDigest;
	}

	public boolean isServer() {
		return server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}

	public String getSymEncrypt() {
		return symEncrypt;
	}

	public void setSymEncrypt(String symEncrypt) {
		this.symEncrypt = symEncrypt;
	}

	public String getNonSymEncryt() {
		return nonSymEncryt;
	}

	public void setNonSymEncryt(String nonSymEncryt) {
		this.nonSymEncryt = nonSymEncryt;
	}

	public String getAuthDigest() {
		return authdigest;
	}

	public void setAuthDigest(String authdigest) {
		this.authdigest = authdigest;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getNonsymBlockSize() {
		return nonsymBlockSize;
	}

	public void setNonsymBlockSize(int nonsymBlockSize) {
		this.nonsymBlockSize = nonsymBlockSize;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getPublic_key() {
		return public_key;
	}

	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}

	public String getRemote_public_key() {
		return remote_public_key;
	}

	public void setRemote_public_key(String remote_public_key) {
		this.remote_public_key = remote_public_key;
	}

	@Override
	public String toString() {
		return "AuthConfig [symEncrypt=" + symEncrypt + ", nonSymEncryt=" + nonSymEncryt + ", authdigest=" + authdigest + ", messageDigest=" + messageDigest + ", timeOut=" + timeOut + ", ip=" + ip
				+ ", port=" + port + ", nonsymBlockSize=" + nonsymBlockSize + ", private_key=" + private_key + ", public_key=" + public_key + ", remote_public_key=" + remote_public_key + ", server="
				+ server + "]";
	}

}
