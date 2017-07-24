package com.github.pasp.context.weave;

import com.github.pasp.core.Pasp;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class WeavableWebAppContext extends XmlWebApplicationContext {

	private static final String KEY_PASP_WEAVE = "pasp.weave";
	private static final String STAT_ON = "on";

	public WeavableWebAppContext() {
		if (STAT_ON.equals(Pasp.getProperty(KEY_PASP_WEAVE))) {
			WeaverClassLoader cl = WeaverClassLoader.getInstance(getClassLoader());
			setClassLoader(cl);
			String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
			if (profile != null && profile.length() > 0) {
				profile += "," + KEY_PASP_WEAVE;
			} else {
				profile = KEY_PASP_WEAVE;
			}
			System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
		}
	}

}
