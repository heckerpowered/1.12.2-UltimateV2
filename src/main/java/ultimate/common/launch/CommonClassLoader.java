package ultimate.common.launch;

public final class CommonClassLoader extends ClassLoader {
    private static final class SingletonHolder {
        private static final CommonClassLoader INSTANCE = new CommonClassLoader();
    }

    public static CommonClassLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Class<?> defineClass(byte[] data) {
        return super.defineClass(null, data, 0, data.length);
    }
}
