package com.example.tool;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class SimpleCookieJar implements CookieJar {
    private final SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "DemoCookiePrefs";
    private static final String PREF_COOKIES = "cookies";

    public SimpleCookieJar(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        // 保存所有 Cookie
        Set<String> cookieSet = new HashSet<>();
        for (Cookie cookie : cookies) {
            cookieSet.add(cookie.toString());
        }
        sharedPreferences.edit().putStringSet(PREF_COOKIES, cookieSet).apply();
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        // 載入已儲存的 Cookie
        List<Cookie> cookies = new ArrayList<>();
        Set<String> cookieSet = sharedPreferences.getStringSet(PREF_COOKIES, new HashSet<>());
        for (String cookieString : cookieSet) {
            Cookie cookie = Cookie.parse(url, cookieString);
            if (cookie != null) {
                cookies.add(cookie);
            }
        }
        return cookies;
    }

    // 清除所有 Cookie (用於登出登入)
    public void clearCookies() {
        sharedPreferences.edit().remove(PREF_COOKIES).apply();
    }
    public boolean hasCookies() {
        Set<String> cookieSet = sharedPreferences.getStringSet(PREF_COOKIES, null);
        return cookieSet != null && !cookieSet.isEmpty();
    }
}