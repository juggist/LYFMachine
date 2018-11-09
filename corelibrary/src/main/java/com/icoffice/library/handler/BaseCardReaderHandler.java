package com.icoffice.library.handler;

import android.os.Handler;

public class BaseCardReaderHandler extends Handler {
	public static enum CareReaderResult{ Unknown, Success, Fail, Reading };
}
