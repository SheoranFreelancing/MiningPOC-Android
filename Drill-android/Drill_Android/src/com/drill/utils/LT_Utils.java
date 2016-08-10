package com.drill.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.security.auth.x500.X500Principal;

public class LT_Utils {

	private static final String TAG = "LT_Utils";
	public static Context appContext;

	public static String getDateTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
		return df.format(new Date());
	}

	public static String convertToYYYYMMDDfromMMDDYYYY(String date) {
		String parts[] = date.split("-");
		if (parts[0].length() < 2)
			parts[0] = "0" + parts[0];
		if(parts.length == 1)
			return "197001" + parts[0];
		if (parts[1].length() < 2)
			parts[1] = "0" + parts[1];
		if(parts.length == 2)
			return "1970" + parts[0] + parts[1];

		return parts[2] + parts[0] + parts[1];
	}

	public static String convertToMMDDYYYYfromYYYYMMDD(String date) {
		try {
			return date.substring(4, 6) + "-" + date.substring(6, 8) + "-"
					+ date.substring(0, 4);
		} catch (Exception e) {
			return date;
		}
	}

	public static String getFormattedDate(long timestampInSec) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"EEE,  d MMM yyyy");
			Date netDate = (new Date(timestampInSec * 1000));
			return formatter.format(netDate);
		} catch (Exception e) {
			return "---";
		}
	}

	public static String getFormattedDate(String dateString) {
		String newFormat = "E, dd MMM yyyy";
		return getFormattedDate(dateString, newFormat);
	}

	public static String getFormattedDate(String dateString, String reqFormat) {
		try {
			LTLog.v(TAG, "Server date to format: " + dateString);
			SimpleDateFormat serverFormatter = new SimpleDateFormat(
					"yyyy:MM:dd:kk:mm:ss");
			Date netDate = serverFormatter.parse(dateString);
			LTLog.v(TAG, "Applying client specific format to: " + netDate);
			SimpleDateFormat clientFormatter = new SimpleDateFormat(reqFormat);
			String newFormat = clientFormatter.format(netDate);
			return newFormat;
		} catch (Exception e) {
			return "---";
		}
	}

	// @SuppressLint("SimpleDateFormat")
	public static String convertMMDDYYYYTofromTimeStamp(long timeStampInSecs) {
		return new SimpleDateFormat("MM/dd/yyyy").format(new Date(
				timeStampInSecs * 1000));
	}

	// @SuppressLint("SimpleDateFormat")
	public static String getMMDDYYYY_Date(long daysOffset) {
		long difference = System.currentTimeMillis() + daysOffset * 24 * 60
				* 60 * 1000;
		Date date = new Date(difference);
		String offsettedDate = new SimpleDateFormat("MM-dd-yyyy").format(date);
		return offsettedDate;
	}

	public static String formatBDayDate(String bdayString) {
		if(bdayString == null)
			bdayString = "1980-01-01";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = null;
		try {
			newDate = format.parse(bdayString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("dd-MMM-yyyy");
		String date = format.format(newDate);
		if(null != date)
			return date;
		return bdayString;
	}

	public static CharSequence getFriendlyTime(long timeStampInSecs,
			long currentServerTime) {
		CharSequence relTime = DateUtils.getRelativeTimeSpanString(
				timeStampInSecs * 1000, currentServerTime * 1000,
				DateUtils.SECOND_IN_MILLIS);
		return relTime;
	}

	public static CharSequence getFriendlyTime(long timeStampInSecs) {
		if (timeStampInSecs < 0) {
			return "Just now";
		}
		long timeStampInMillis = timeStampInSecs * 1000;
		long currentTimeStampMillis = System.currentTimeMillis();
		if (currentTimeStampMillis <= timeStampInMillis) {
			return "Just now";
		}
		// System.out.println("TIMEZONERAWOFFSET(mins):::: " +
		// ((TimeZone.getDefault().getRawOffset()/1000)/60));
		CharSequence relTime = DateUtils.getRelativeTimeSpanString(
				timeStampInMillis, currentTimeStampMillis,
				DateUtils.SECOND_IN_MILLIS);
		if (((String) relTime).startsWith("0 second")) {
			return "Just now";
		} else {
			return relTime;
		}
	}

	public static CharSequence getFriendlyTimeForMessageList(
			long timeStampInSecs) {
		if (timeStampInSecs < 0) {
			return "Just now";
		}
		long timeStampInMillis = timeStampInSecs * 1000;
		long currentTimeStampMillis = System.currentTimeMillis();
		if (currentTimeStampMillis <= timeStampInMillis) {
			return "Just now";
		}
		CharSequence relTime = DateUtils.formatSameDayTime(timeStampInMillis,
				currentTimeStampMillis, DateFormat.DEFAULT,
				DateFormat.SHORT);
		return relTime;
	}

	public static String stringArrayToCommaSaparatedString(String array[]) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i == 0)
				builder.append(array[i]);
			else
				builder.append(',').append(array[i]);
		}
		return builder.toString();
	}

	public static String convertDatePickerDate(String dateString) {
		String newDateStr = "";
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = form.parse(dateString);
			SimpleDateFormat postFormater = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
			newDateStr = postFormater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return dateString;
		}
		return newDateStr;
	}

	public static String getYYYYMMDDFormattedDate(String dateString) {
		String newDateStr = "";
		SimpleDateFormat form = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_SERVER);
		form.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		try {
			date = form.parse(dateString);
			SimpleDateFormat postFormater = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
			newDateStr = postFormater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return dateString;
		}
		return newDateStr;
	}

	public static String serverFormattedDate(String dateString, boolean endOfDay) {
		String newDateStr = "";
		SimpleDateFormat form = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date date = null;
		try {
			date = form.parse(dateString);
			if(endOfDay) {
				date.setTime(date.getTime() + (Constants.DAY_IN_MILLIS - 1));
			}
			SimpleDateFormat postFormater = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_SERVER);
			postFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
			newDateStr = postFormater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return dateString;
		}
		return newDateStr;
	}

	public static Date getTimeDate(String time) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			date = sdf.parse(time);
		}
		catch (ParseException e) {
		}
		return date;
	}

	public static Date getDaysDate(String time) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		try {
			date = sdf.parse(time);
		}
		catch (ParseException e) {
		}
		return date;
	}

	private static final X500Principal DEBUG_DN = new X500Principal(
			"CN=Android Debug,O=Android,C=US");

	public static boolean isReleaseBuild() {
		boolean debuggable = false;

		try {
			PackageInfo pinfo = appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature signatures[] = pinfo.signatures;

			for (int i = 0; i < signatures.length; i++) {
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				ByteArrayInputStream stream = new ByteArrayInputStream(
						signatures[i].toByteArray());
				X509Certificate cert = (X509Certificate) cf
						.generateCertificate(stream);
				debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
				if (debuggable)
					break;
			}

		} catch (NameNotFoundException e) {
			// debuggable variable will remain false
		} catch (CertificateException e) {
			// debuggable variable will remain false
		}

		if (debuggable) {
			//RetroHttpManager.RELEASE_BUILD = false;
		} else {
			//RetroHttpManager.RELEASE_BUILD = true;
		}
		return false;
	}

	public static void makeLinkClickable(final Activity activity, SpannableStringBuilder strBuilder, final URLSpan span) {
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);
		Log.e("LT_Utils", "creating clickable span for:" + span.getURL() + "  --- " + flags + "start:" + start + "  end:" + end);
		final ClickableSpan clickable = new ClickableSpan() {
			String url = span.getURL();

			public void onClick(View view) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				activity.startActivity(browserIntent);
				Log.d("LT_Utils", "linked tap:" + url);
			}
		};
		strBuilder.removeSpan(span);
		strBuilder.setSpan(clickable, start, end, flags);
	}
	
	public static void setTextViewHTML(final Activity activity, TextView text, String html)
	{
	    CharSequence sequence = Html.fromHtml(html);
	        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
	        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);   
	        for(URLSpan span : urls) {
	            makeLinkClickable(activity, strBuilder, span);
	        }
	    text.setText(strBuilder);       
	}
	
}
