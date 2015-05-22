package com.yisi.yisiHome.baseFragment;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.MainActivity;
import com.yisi.yisiHome.utils.Constants;

public class DateDialogFragment extends DialogFragment {
	private View rootView;
	private static DateDialogFragment dialogFragment;
	private Dialog dialog;
	private String type = Constants.DATE_TIME_TYPE_MINUTES;

	private DateDialogFragment() {
		super();
	}

	public static DateDialogFragment newInstance() {
		if (dialogFragment == null) {
			dialogFragment = new DateDialogFragment();
		}
		return dialogFragment;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dialog = getDialog();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		rootView = inflater.inflate(R.layout.wheel_view_date_time_layout,
				container, false);
		init();
		return rootView;
	}

	// Time scrolled flag
	private boolean timeScrolled = false;

	public void init() {
		Calendar calendar = Calendar.getInstance();

		final WheelView month = (WheelView) rootView.findViewById(R.id.month);
		final WheelView year = (WheelView) rootView.findViewById(R.id.year);
		final WheelView day = (WheelView) rootView.findViewById(R.id.day);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};

		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = new String[] { "01", "02", "03", "04", "05", "06",
				"07", "08", "09", "10", "11", "12" };
		month.setViewAdapter(new DateArrayAdapter(getActivity(), months,
				curMonth));
		month.setCurrentItem(curMonth);
		month.getViewAdapter().setCurrentIndex(curMonth);
		month.addChangingListener(listener);

		// year
		int curYear = calendar.get(Calendar.YEAR);
		year.setViewAdapter(new DateNumericAdapter(getActivity(), curYear - 10,
				curYear + 10, 0));

		year.setCurrentItem(10);
		year.getViewAdapter().setCurrentIndex(10);
		year.addChangingListener(listener);

		// day
		updateDays(year, month, day);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		final WheelView hours = (WheelView) rootView.findViewById(R.id.hour);
		hours.setViewAdapter(new DateNumericAdapter(getActivity(), 0, 23, 0));
		final WheelView second = (WheelView) rootView.findViewById(R.id.second);
		second.setViewAdapter(new DateNumericAdapter(getActivity(), 0, 59, 0,
				"%02d"));

		final WheelView mins = (WheelView) rootView.findViewById(R.id.mins);
		mins.setViewAdapter(new DateNumericAdapter(getActivity(), 0, 59, 0,
				"%02d"));
		mins.setCyclic(true);
		year.setCyclic(true);
		month.setCyclic(true);
		day.setCyclic(true);
		hours.setCyclic(true);

		rootView.findViewById(R.id.confirm_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						Intent data = getActivity().getIntent();
						String time = year.getViewAdapter().getCurentValue(
								year.getCurrentItem())
								+ "-"
								+ month.getViewAdapter().getCurentValue(
										month.getCurrentItem())
								+ "-"
								+ day.getViewAdapter().getCurentValue(
										day.getCurrentItem())
								+ " "
								+ hours.getViewAdapter().getCurentValue(
										hours.getCurrentItem())
								+ ":"
								+ mins.getViewAdapter().getCurentValue(
										mins.getCurrentItem())
								+ ":"
								+ second.getViewAdapter().getCurentValue(
										second.getCurrentItem());
						if (type.equals(Constants.DATE_TIME_TYPE_DAY)) {// 日
							time = time.substring(0, 10);
						} else if (type
								.equals(Constants.DATE_TIME_TYPE_MINUTES)) {// 分
							time = time.substring(0, 16);
						} else if (type.equals(Constants.DATE_TIME_TYPE_MONTH)) {// 月
							time = time.substring(0, 7);
						} else if (type.equals(Constants.DATE_TIME_TYPE_YEAR)) {// 年
							time = time.substring(0, 4);
						}
						data.putExtra(Constants.RESULT, time);
						((MainActivity) getActivity()).getCurFragment()
								.onResult(data);
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
		rootView.findViewById(R.id.cancel_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						Intent data = getActivity().getIntent();
						// data.putExtra("result", data.getStringExtra("result")
						// + " 00:00");
						((MainActivity) getActivity()).getCurFragment()
								.onResult(data);
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});

		// set current time
		Calendar c = Calendar.getInstance();
		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curMinutes = c.get(Calendar.MINUTE);
		int curSecond = c.get(Calendar.SECOND);

		hours.setCurrentItem(curHours);
		hours.getViewAdapter().setCurrentIndex(curHours);
		mins.setCurrentItem(curMinutes);
		mins.getViewAdapter().setCurrentIndex(curMinutes);
		mins.setCurrentItem(curSecond);
		mins.getViewAdapter().setCurrentIndex(curSecond);

		// add listeners
		addChangingListener(mins, "min");
		addChangingListener(hours, "hour");
		addChangingListener(second, "second");

		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
				}
			}
		};
		hours.addChangingListener(wheelListener);
		mins.addChangingListener(wheelListener);
		second.addChangingListener(wheelListener);

		OnWheelClickedListener click = new OnWheelClickedListener() {
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		};
		hours.addClickingListener(click);
		mins.addClickingListener(click);
		second.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				wheel.getViewAdapter().setCurrentIndex(wheel.getCurrentItem());
				wheel.getViewAdapter().notifyDataSetChanged();
				;
			}
		};

		year.addScrollingListener(scrollListener);
		month.addScrollingListener(scrollListener);
		day.addScrollingListener(scrollListener);
		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);
		second.addScrollingListener(scrollListener);
		if (type.equals(Constants.DATE_TIME_TYPE_DAY)) {// 日
			hours.setVisibility(View.GONE);
			mins.setVisibility(View.GONE);
			second.setVisibility(View.GONE);
		} else if (type.equals(Constants.DATE_TIME_TYPE_MINUTES)) {// 分
			second.setVisibility(View.GONE);
		} else if (type.equals(Constants.DATE_TIME_TYPE_MONTH)) {// 月
			day.setVisibility(View.GONE);
			hours.setVisibility(View.GONE);
			mins.setVisibility(View.GONE);
			second.setVisibility(View.GONE);
		} else if (type.equals(Constants.DATE_TIME_TYPE_YEAR)) {// 年
			month.setVisibility(View.GONE);
			day.setVisibility(View.GONE);
			hours.setVisibility(View.GONE);
			mins.setVisibility(View.GONE);
			second.setVisibility(View.GONE);
		}
	}

	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(getActivity(), 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		day.getViewAdapter().setCurrentIndex(
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.getViewAdapter().notifyDataSetChanged();
	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			this(context, minValue, maxValue, current, null);
		}

		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current, String format) {
			super(context, minValue, maxValue, format);
			this.currentValue = current;
			setTextSize(20);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(Color.RED);
				view.getPaint().setFakeBoldText(true);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}

		@Override
		public void setCurrentIndex(int index) {
			currentValue = index;
		}

		@Override
		public int getCurrentIndex() {
			return currentValue;
		}
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(20);
		}

		@Override
		public void setCurrentIndex(int index) {
			currentValue = index;
		}

		@Override
		public int getCurrentIndex() {
			return currentValue;
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.getPaint().setFakeBoldText(true);
				view.setTextColor(Color.RED);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}
}
