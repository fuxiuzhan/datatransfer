package com.fxz.channelswitcher.datatransferserver.auth.impl;

import com.fxz.channelswitcher.datatransferserver.auth.auth.INonSymEncrypt;
import com.fxz.channelswitcher.datatransferserver.auth.auth.Utils;
import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSA implements INonSymEncrypt {

	String private_key = null, public_key = null;
	Cipher cipher;

	@Override
	public void setKeys(String privatekey, String publickey) {
		this.private_key = privatekey;
		this.public_key = publickey;
	}

	@Override
	public synchronized void init(int block) throws EncryptExcepton {
		if (private_key != null && public_key != null) {
			return;
		}
		try {
			cipher = Cipher.getInstance("RSA/ECB/NoPadding");// , new
			// org.bouncycastle.jce.provider.BouncyCastleProvider());
			// TODO Auto-generated method stub
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");// ,
																				// new
																				// org.bouncycastle.jce.provider.BouncyCastleProvider());
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			public_key = getKeyString(publicKey);
			private_key = getKeyString(privateKey);
		} catch (Exception e) {
			// TODO: handle exception
			throw new EncryptExcepton(e);
		}
	}

	public synchronized byte[] encryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return rsaEncrypt_Prik(getPrivateKey(privateKey), buffer);
		// return encrypt(getPrivateKey(privateKey), buffer);
	}

	@Override
	public synchronized byte[] encryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return rsaEncrypt_Pubk(getPublicKey(publicKey), buffer);
		// return encrypt(getPublicKey(publicKey), buffer);
	}

	@Override
	public synchronized byte[] decryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return rsaDecrypt_Prik(getPrivateKey(privateKey), buffer);
		// return decrypt(getPrivateKey(privateKey), buffer);
	}

	@Override
	public synchronized byte[] decryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return rsaDecrypt_Pubk(getPublicKey(publicKey), buffer);
		// return decrypt(getPublicKey(publicKey), buffer);
	}

	@Override
	public String getPrivateKey() {
		// TODO Auto-generated method stub
		return private_key;
	}

	@Override
	public String getPublicKey() {
		// TODO Auto-generated method stub
		return public_key;
	}

	private String getKeyString(Key key) throws EncryptExcepton {
		byte[] keyBytes = key.getEncoded();
		String s = Utils.Byte2Hex(keyBytes);
		return s;
	}

	private PublicKey getPublicKey(String key) throws EncryptExcepton {
		try {
			byte[] keyBytes;
			keyBytes = Utils.Hex2Byte(key);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (Exception e) {
			throw new EncryptExcepton(e);// TODO: handle exception
		}
	}

	private PrivateKey getPrivateKey(String key) throws EncryptExcepton {
		try {
			byte[] keyBytes;
			keyBytes = Utils.Hex2Byte(key);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			// TODO: handle exception
			throw new EncryptExcepton(e);
		}
	}

	private byte[] rsaDecrypt_Pubk(PublicKey publicKey, byte[] enbytes) throws EncryptExcepton {
		try {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			int size = enbytes.length / 128;
			byte[] rtn = null;
			for (int i = 0; i < size; i++) {
				byte[] temp = cipher.doFinal(Utils.subBytes(enbytes, i * 128, 128));
				rtn = Utils.bytesComb(rtn, temp);
			}
			return rtn;
		} catch (Exception ex) {
			throw new EncryptExcepton(ex);
		}
	}

	private byte[] rsaDecrypt_Prik(PrivateKey privateKey, byte[] enbytes) throws EncryptExcepton {
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int size = enbytes.length / 128;
			byte[] rtn = null;
			for (int i = 0; i < size; i++) {
				byte[] temp = cipher.doFinal(Utils.subBytes(enbytes, i * 128, 128));
				rtn = Utils.bytesComb(rtn, temp);
			}
			return rtn;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EncryptExcepton(ex);
		}
	}

	private byte[] rsaEncrypt_Prik(PrivateKey privateKey, byte[] context) throws EncryptExcepton {
		int size = context.length / 117;
		byte[] rtn = null;
		if (context.length % 117 != 0) {
			rtn = new byte[(size + 1) * 128];
		} else {
			rtn = new byte[size * 128];
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			for (int i = 0; i < size; i++) {
				byte[] temp = new byte[117];
				System.arraycopy(context, i * 117, temp, 0, 117);
				byte[] enbytes = cipher.doFinal(temp);
				System.arraycopy(enbytes, 0, rtn, i * 128, 128);
			}
			if (context.length % 117 != 0) {
				byte[] temp = new byte[context.length % 117];
				System.arraycopy(context, size * 117, temp, 0, context.length % 117);
				byte[] enbytes = cipher.doFinal(temp);
				System.arraycopy(enbytes, 0, rtn, size * 128, 128);
			}
			return rtn;
		} catch (Exception ex) {
			throw new EncryptExcepton(ex);
		}

	}

	@SuppressWarnings("unused")
	private byte[] decrypt(Key key, byte[] raw) throws EncryptExcepton {
		try {
			cipher = Cipher.getInstance("RSA/ECB/NoPadding");// , new
																// org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, key);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;
			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

	@SuppressWarnings("unused")
	private byte[] encrypt(Key key, byte[] data) throws EncryptExcepton {
		try {
			cipher = Cipher.getInstance("RSA/ECB/NoPadding");// , new
																// org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 获得加密块大小，如:加密前数据为128个byte，而key_size=1024 加密块大小为127
			// byte,加密后为128个byte;
			// 因此共有2个加密块，第一个127 byte第二个为1个byte
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize) {
					cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
				} else {
					cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
				}
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
				// ，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

	private byte[] rsaEncrypt_Pubk(PublicKey publicKey, byte[] context) throws EncryptExcepton {
		int size = context.length / 117;
		byte[] rtn = null;
		if (context.length % 117 != 0) {
			rtn = new byte[(size + 1) * 128];
		} else {
			rtn = new byte[size * 128];
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			for (int i = 0; i < size; i++) {
				byte[] temp = new byte[117];
				System.arraycopy(context, i * 117, temp, 0, 117);
				byte[] enbytes = cipher.doFinal(temp);
				System.arraycopy(enbytes, 0, rtn, i * 128, 128);
			}
			if (context.length % 117 != 0) {
				byte[] temp = new byte[context.length % 117];
				System.arraycopy(context, size * 117, temp, 0, context.length % 117);
				byte[] enbytes = cipher.doFinal(temp);
				System.arraycopy(enbytes, 0, rtn, size * 128, 128);
			}
			return rtn;
		} catch (Exception ex) {
			throw new EncryptExcepton(ex);
		}

	}

	public static void main(String[] args) throws EncryptExcepton {
		String pubkey = "30819F300D06092A864886F70D010101050003818D003081890281810087EEC621324404829B4C96BBC2D000167052D408213BB362AF3B6D98FAD61F01E94B9FC9749792BFFE612A6D4E18AFE0D3BA7C827E5763550D5624BBB46074A9E5AC520C69FF776D38956D0BA899CBD809937E058AE54D3810601D99790D059C4C5833236A26F49A45B95828D9C89BB734AFFC31DA8D0943F82C5C3CE84D0F5B0203010001";
		String en_str = "1A43782FD6F78A48564687F52A6B812252B2E9D1142240F03409211676996FA53155223C82DA09CB7D161EEB4D3E0906C339C6ABAF39EA72358864D57436AC47A9EDF1E91283630AA36EA2E6B9316FFFBFA5C079E3935A991CCA888434F321D07933943D1E4AB00B521E9E96F77BE74F83D4634EEE4540A0904E803A1FC7240D7B9A38546E42D0CE80325F9F2E2D9D9BBE734BB0D4C0106045B08C47AF88CCF5D5C43F0489B72B499469A029D86FE132E686AE99F79D309C29ABF3A9DF795381D0F3F115813C672924DEDF617D89AE9AFD82870D13958102E7B3A7B0111B8FCB2D0C183B6391D69FFBA7A5A27264FC7E75D35BB4E5860DC4F12C7CC41604BAE6";

		RSA rsa = new RSA();
		rsa.init(1024);
		String rez = Utils.Byte2Hex(rsa.decryptByPublicKey(Utils.Hex2Byte(en_str), pubkey));
		System.out.println(rez);
		System.out.println(Utils.Byte2Hex(rsa.encryptByPrivateKey(Utils.Hex2Byte(en_str), rsa.private_key)));
	}
}
