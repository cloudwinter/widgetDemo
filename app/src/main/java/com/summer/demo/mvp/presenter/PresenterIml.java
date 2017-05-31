package com.summer.demo.mvp.presenter;

import com.summer.demo.mvp.model.ModelServiceImpl;
import com.summer.demo.mvp.view.ViewInterface;

/**
 * Created by xiayundong on 2017/5/31.
 */

public class PresenterIml implements PresenterInterface {

	private ViewInterface mViewInterface;
	private ModelServiceImpl mModelService;

	public PresenterIml(ViewInterface viewInterface) {
		mViewInterface = viewInterface;
		mModelService = new ModelServiceImpl();
	}

	@Override
	public void startDown(String url) {
		mModelService.download(url, new ModelServiceImpl.ModelCallBack() {
			@Override
			public void start() {
				mViewInterface.startDown();
			}

			@Override
			public void download(int position) {
				mViewInterface.showProgress(position);
			}

			@Override
			public void downSuccess() {
				mViewInterface.downCompleted();
			}

			@Override
			public void downError(String msg) {
				mViewInterface.downError(msg);
			}
		});
	}

	@Override
	public void stopDown() {
		mModelService.stopDown();
	}
}
