package luo.library.base.base;

/**
 *
 */

public class BaseAndroid {
    public static BaseConfig baseConfig;

    public static void init(BaseConfig config) {
        baseConfig = config;
    }

    public static BaseConfig getBaseConfig() {
        if (baseConfig == null) {
            throw new IllegalArgumentException("请先调用init方法");
        }
        return baseConfig;
    }
}
