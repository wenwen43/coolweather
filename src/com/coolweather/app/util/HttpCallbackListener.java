package com.coolweather.app.util;

//�˽ӿ������ص����񷵻صĽ��

public interface HttpCallbackListener {

	void onFinish(String response);
	void onError(Exception e);
}
