package com.github.pasp.context.weave;

import java.lang.instrument.ClassFileTransformer;
import java.util.List;

import com.github.pasp.core.Pasp;
import org.springframework.core.OverridingClassLoader;
import org.springframework.instrument.classloading.WeavingTransformer;

public class WeaverClassLoader extends OverridingClassLoader {
	private static final String KEY_PASP_WEAVE_EXCLUDES = "pasp.weave.excludes";
	private static WeaverClassLoader instance;

	static {
		if (parallelCapableClassLoaderAvailable) {
			ClassLoader.registerAsParallelCapable();
		}
	}

	private final WeavingTransformer weavingTransformer;

	public static WeaverClassLoader getInstance(ClassLoader parent) {
		if (instance != null) {
			return instance;
		}
		instance = new WeaverClassLoader(parent);
		doInternalExclude();
		doCustomExclude();
		return instance;
	}

	private static void doCustomExclude() {
		String prop = Pasp.getProperty(KEY_PASP_WEAVE_EXCLUDES);
		if (prop == null || prop.length() == 0) {
			return;
		}
		String[] split = prop.split(",| |;");
		for (String pack : split) {
			instance.excludePackage(pack);
		}
	}

	private static void doInternalExclude() {
		WeaveConfig config = WeaveConfigParser.getConfig();
		if (config == null) {
			return;
		}
		WeaveConfig.Excludes excludes = config.getExcludes();
		if (excludes == null) {
			return;
		}
		List<WeaveConfig.Exclude> list = excludes.getExcludes();
		if (list == null) {
			return;
		}
		for (WeaveConfig.Exclude e : list) {
			instance.excludePackage(e.getPack());
		}
	}

	/**
	 * Create a new SimpleInstrumentableClassLoader for the given ClassLoader.
	 * 
	 * @param parent
	 *            the ClassLoader to build an instrumentable ClassLoader for
	 */
	private WeaverClassLoader(ClassLoader parent) {
		super(parent);
		this.weavingTransformer = new WeavingTransformer(parent);
	}

	/**
	 * Add a {@link ClassFileTransformer} to be applied by this ClassLoader.
	 * 
	 * @param transformer
	 *            the {@link ClassFileTransformer} to register
	 */
	public void addTransformer(ClassFileTransformer transformer) {
		this.weavingTransformer.addTransformer(transformer);
	}

	@Override
	protected byte[] transformIfNecessary(String name, byte[] bytes) {
		return this.weavingTransformer.transformIfNecessary(name, bytes);
	}

}
