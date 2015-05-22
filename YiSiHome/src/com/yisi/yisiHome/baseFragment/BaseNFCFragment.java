package com.yisi.yisiHome.baseFragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.YisiApp;
import com.yisi.yisiHome.entity.EntityICCard;

public class BaseNFCFragment extends BaseFragment implements OnClickListener {
	private String pwd = "201003220000";
	private String encode = "gbk";
	// private String pwd = "FFFFFFFFFFFF";
	private int step = 1;// 几个块为一单位
	private int id_card = 1, id_car_num = 1 + step, id_name = 1 + 2 * step,
			id_mat = 1 + 3 * step, id_weigh_time = 1 + 4 * step,
			id_phone = 1 + 5 * step, id_operator = 1 + 6 * step,
			id_optTime = 1 + 7 * step;
	TextView icId, cardId, carNum, name, mat, weightTime, phoneNum, operator,
			optTime;
	private Intent m_intent;
	private Tag m_tagFromIntent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_nfc_lauout, container,
				false);
		initview();
		processAdapterAction(mainActivity.getIntent());
		return rootView;
	}

	private void initview() {
		icId = (TextView) rootView.findViewById(R.id.ic_id);
		cardId = (TextView) rootView.findViewById(R.id.cardId);
		carNum = (TextView) rootView.findViewById(R.id.car_num);
		name = (TextView) rootView.findViewById(R.id.name);
		mat = (TextView) rootView.findViewById(R.id.mat);
		weightTime = (TextView) rootView.findViewById(R.id.weigh_time);
		phoneNum = (TextView) rootView.findViewById(R.id.phone_owner);
		operator = (TextView) rootView.findViewById(R.id.operator);
		optTime = (TextView) rootView.findViewById(R.id.optTime);

		rootView.findViewById(R.id.next).setOnClickListener(this);
		rootView.findViewById(R.id.conform).setOnClickListener(this);
	}

	private void processIntent(Intent intent) {
		m_intent = intent;
		// 取出封装在intent中的TAG
		m_tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		byte[] myNFCID = m_intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		// 初始化数据
		icId.setText(new String(bytesToHexString(myNFCID)));
		cardId.setText(readSector(id_card));
		carNum.setText(readSector(id_car_num));
		name.setText(readSector(id_name));
		mat.setText(readSector(id_mat));
		weightTime.setText(readSector(id_weigh_time));
		phoneNum.setText(readSector(id_phone));
		operator.setText(readSector(id_operator));
		optTime.setText(readSector(id_optTime));
	}

	/***********************************************************************/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.conform:// 修改数据
			writeSector(id_card, cardId.getText().toString());
			writeSector(id_name, name.getText().toString());
			writeSector(id_mat, mat.getText().toString());
			writeSector(id_car_num, carNum.getText().toString());
			writeSector(id_weigh_time, weightTime.getText().toString());
			writeSector(id_phone, phoneNum.getText().toString());
			writeSector(id_operator, operator.getText().toString());
			writeSector(id_optTime, optTime.getText().toString());
			// testWriteAll();
			break;
		case R.id.next:
//			testReadAll();
			// mainActivity.startActivity(new Intent(mainActivity,
			// TakPicActivity.class));
			if (TextUtils.isEmpty(cardId.getText().toString())) {
				YisiApp.tell(mainActivity, "请先扫卡");
				return;
			}
			EntityICCard ic = new EntityICCard();
			ic.setCardId(cardId.getText().toString());
			ic.setCarNum(carNum.getText().toString());
			ic.setPhoneNum(phoneNum.getText().toString());
			ic.setMaterial(mat.getText().toString());
			ic.setCustom(name.getText().toString());
			ic.setWeightTime(weightTime.getText().toString());
			ic.setOperator(operator.getText().toString());
			ic.setOperateTime(System.currentTimeMillis());
			setValue(mainActivity.getIntent(), "ic", JSON.toJSONString(ic));
			mainActivity.otherBackableFragment(new TakPicFragment());
			break;
		default:
			break;
		}
	}

	private void writeBlock(int blockIndex, String src) {
		byte[] bytes=new byte[16];
		try {
			byte[] arr=src.trim().getBytes(encode);
			System.arraycopy(arr, 0, bytes, 0, arr.length>16?16:arr.length);
			writeBlock(blockIndex, bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	private void writeBlock(int blockIndex, byte[] myByte) {
		// byte[] myByte = stringToBytes(msg);
		// 按需要分成合适的字节，多的舍弃
		if (m_tagFromIntent != null) {
			MifareClassic mfc = MifareClassic.get(m_tagFromIntent);
			// String metaInfo="";
			// strUI += "卡片类型：" + mfc.getType() + "\n共" +
			// mfc.getSectorCount() + "个扇区\n共"
			// + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize()
			// + "B\n";
			if (mfc != null) {
				try {
					mfc.connect();
					try {
						int bSec = mfc.blockToSector(blockIndex);
						if (bSec < mfc.getSectorCount()) {
							if (mfc.authenticateSectorWithKeyA(bSec,
									stringToBytes(pwd))) {
								if (blockIndex
										% mfc.getBlockCountInSector(blockIndex) == mfc
										.getBlockCountInSector(blockIndex) - 1) {
									Log.i(getClass().getSimpleName(),
											blockIndex + "块是控制块");
								} else {
									mfc.writeBlock(blockIndex, myByte);
									Log.i(getClass().getSimpleName(),
											blockIndex + "块写入成功");
								}
							} else {
								Log.i(getClass().getSimpleName(), blockIndex
										+ "块密码错误");
							}
						} else {
							Log.i(getClass().getSimpleName(),
									blockIndex + "块读取bSec:" + bSec
											+ "<>mfc.getSectorCount():"
											+ mfc.getSectorCount());
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally{
						mfc.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	// private String readFrom(int blockIndex) {
	// byte[] byteArr = new byte[16 * step];
	// for (int i = blockIndex; i < blockIndex + step; i++) {
	// byte[] src = readBlock(i);
	// System.arraycopy(src, 0, byteArr, (i - blockIndex) * 16, src.length);
	// }
	// try {
	// return new String(byteArr, encode).trim();
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// return "";
	// }
	//
	// }

	private void writeSector(int sectorIndex, String msg) {
		byte[] myByte;
		try {
			myByte = msg.trim().getBytes(encode);
		} catch (Exception e1) {
			System.out.println("encoding err:" + encode);
			e1.printStackTrace();
			return;
		}
		byte[] byteArr = new byte[0];
		if (m_intent != null) {
			if (m_tagFromIntent != null) {
				MifareClassic mfc = MifareClassic.get(m_tagFromIntent);
				// String metaInfo="";
				// strUI += "卡片类型：" + mfc.getType() + "\n共" +
				// mfc.getSectorCount() + "个扇区\n共"
				// + mfc.getBlockCount() + "个块\n存储空间: " +
				// mfc.getSize() + "B\n";
				if (mfc != null) {
					try {
						mfc.connect();
						int start = mfc.sectorToBlock(sectorIndex);
						int count = mfc.getBlockCountInSector(sectorIndex);
						mfc.close();
						byteArr = new byte[16 * (count - 1)];
						System.arraycopy(myByte, 0, byteArr, 0, myByte.length);
						for (int i = 0; i < count - 1; i++) {
//							mfc.connect();
							byte[] writer=new byte[16];
							System.arraycopy(byteArr, i*16, writer, 0, writer.length);
//							mfc.writeBlock(start + i, writer);
//							System.out.println("write:"+(start+i)+":<>"+Arrays.toString(writer));
//							System.out.println("write success:"+(start+i)+":<>"+i);
//							mfc.close();
							writeBlock(start+i, writer);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						try {
							mfc.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private String readSector(int sectorIndex) {
		byte[] byteArr = new byte[0];
		if (m_intent != null) {
			if (m_tagFromIntent != null) {
				MifareClassic mfc = MifareClassic.get(m_tagFromIntent);
				// String metaInfo="";
				// strUI += "卡片类型：" + mfc.getType() + "\n共" +
				// mfc.getSectorCount() + "个扇区\n共"
				// + mfc.getBlockCount() + "个块\n存储空间: " +
				// mfc.getSize() + "B\n";
				if (mfc != null) {
					try {
						mfc.connect();
						int start = mfc.sectorToBlock(sectorIndex);
						int count = mfc.getBlockCountInSector(sectorIndex);
						byteArr = new byte[16 * (count - 1)];
						for (int i = 0; i < count - 1; i++) {
							if (mfc.authenticateSectorWithKeyA(sectorIndex,
									stringToBytes(pwd))) {
								byte[] src = mfc.readBlock(start + i);
								System.arraycopy(src, 0, byteArr, (i) * 16,
										src.length);
							} else {
								Log.i(getClass().getSimpleName(), (start + i)
										+ "块密码错误");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						try {
							mfc.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		try {
			return new String(byteArr, encode).trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}

	}

	private String readFromBlock(int blockIndex) {
		try {
			return new String(readBlock(blockIndex), encode).trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	private byte[] readBlock(int blockIndex) {
		byte[] result = null;
		if (m_intent != null) {
			if (m_tagFromIntent != null) {
				MifareClassic mfc = MifareClassic.get(m_tagFromIntent);
				// String metaInfo="";
				// strUI += "卡片类型：" + mfc.getType() + "\n共" +
				// mfc.getSectorCount() + "个扇区\n共"
				// + mfc.getBlockCount() + "个块\n存储空间: " +
				// mfc.getSize() + "B\n";
				if (mfc != null) {
					try {
						mfc.connect();
						int bSec = mfc.blockToSector(blockIndex);
						if (bSec < mfc.getSectorCount()) {
							if (mfc.authenticateSectorWithKeyA(bSec,
									stringToBytes(pwd))) {
								result = mfc.readBlock(blockIndex);
								Log.i(getClass().getSimpleName(), blockIndex
										+ "块读成功");
							} else {
								Log.i(getClass().getSimpleName(), blockIndex
										+ "块密码错误");
							}
						} else {
							Log.i(getClass().getSimpleName(),
									blockIndex + "块读取bSec:" + bSec
											+ "<>mfc.getSectorCount():"
											+ mfc.getSectorCount());
						}
					} catch (IOException e) {
						Log.i(getClass().getSimpleName(), blockIndex + "块读异常");
						e.printStackTrace();
					}finally{
						try {
							mfc.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		if (result != null) {
			return result;
		} else {
			return new byte[0];
		}
	}

	@Override
	public void doNewIntent(Intent intent) {
		processIntent(intent);
		super.doNewIntent(intent);
	}

	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	private byte[] stringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		// hexString = hexString.toLowerCase(Locale.CHINA);
		hexString = hexString.toUpperCase(Locale.CHINA);
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public void testReadAll() {
//		if (m_tagFromIntent == null) {
//			return;
//		}
//		MifareClassic mfc = MifareClassic.get(m_tagFromIntent);
//		for (int i = 0; i < mfc.getSectorCount(); i++) {
//			System.out.println(readSector(i));
//		}
//		writeBlock(10, "test");
//		System.out.println(readFromBlock(10));
	}

	// public void testWriteAll() {
	// // 一块包括四扇区，最后一个扇区写密码，不能操作
	// MifareClassic mfc = MifareClassic.get(m_tagFromIntent);
	// // String metaInfo="";
	// // strUI += "卡片类型：" + mfc.getType() + "\n共" +
	// // mfc.getSectorCount() + "个扇区\n共"
	// // + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize()
	// // + "B\n";
	// long now = System.currentTimeMillis();
	// if (mfc != null) {
	// for (int i = 0; i < mfc.getBlockCount(); i++) {
	// writeTo(i,
	// "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
	// System.out.println(i + " block finished");
	// // for (int j = 0; j < mfc.getSectorCount()-1; j++) {
	// // }
	//
	// }
	// }
	// System.out.println(((System.currentTimeMillis() - now) / 1000)
	// + "second");
	// }

	// 字符序列转换为16进制字符串
	private char[] bytesToHexString(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
		}
		return buffer;
	}

	private void processAdapterAction(Intent intent) {
		// 当系统检测到tag中含有NDEF格式的数据时，且系统中有activity声明可以接受包含NDEF数据的Intent的时候，系统会优先发出这个action的intent。
		// 得到是否检测到ACTION_NDEF_DISCOVERED触发 序号1
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			processIntent(intent);
			return;
		}
		// 当没有任何一个activity声明自己可以响应ACTION_NDEF_DISCOVERED时，系统会尝试发出TECH的intent.即便你的tag中所包含的数据是NDEF的，但是如果这个数据的MIME
		// type或URI不能和任何一个activity所声明的想吻合，系统也一样会尝试发出tech格式的intent，而不是NDEF.
		// 得到是否检测到ACTION_TECH_DISCOVERED触发 序号2
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			processIntent(intent);
			return;
		}
		// 当系统发现前两个intent在系统中无人会接受的时候，就只好发这个默认的TAG类型的
		// 得到是否检测到ACTION_TAG_DISCOVERED触发 序号3
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			processIntent(intent);
			return;
		}
	}
}
