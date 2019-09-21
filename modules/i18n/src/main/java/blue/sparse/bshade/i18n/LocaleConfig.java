package blue.sparse.bshade.i18n;

import blue.sparse.bshade.i18n.value.LocaleValue;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocaleConfig {

	private static final String DEFAULT_CODE = "en_us";
	private static final Map<String, LocaleConfig> map = new HashMap<>();

	private static Map<String, LocaleValue> defaults;

	private String code;
	private FileConfiguration config;
//	private Properties properties;

	private LocaleConfig(String code) {
		this.code = code;
		this.config = new YamlConfiguration();

		if(code.equalsIgnoreCase(DEFAULT_CODE)) {
			YamlConfiguration defaultsConfig = new YamlConfiguration();
			for (Map.Entry<String, LocaleValue> entry : defaults.entrySet()) {
				defaultsConfig.set(entry.getKey(), entry.getValue().raw());
			}
			this.config.setDefaults(defaultsConfig);
			this.config.options().copyDefaults(true);
		}

		File file = new File(getLangFolder(), code+".yml");
		if(file.exists()) {
			try {
				this.config.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}

		try {
			this.config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		this.properties = new Properties();
//
//		JavaPlugin plugin = getPlugin();
//		String fileName = code + ".lang";
//
//		InputStream resource = plugin.getResource("lang/" + fileName);
//		if(resource != null) {
//			try {
//				properties.load(resource);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		File file = new File(getLangFolder(), fileName);
//		try(InputStream fileIn = new FileInputStream(file)) {
//			properties.load(fileIn);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public static LocaleConfig get(Player player) {
		return get(getLocaleCode(player));
	}

	public static LocaleConfig get(String code) {
		return map.computeIfAbsent(code, LocaleConfig::new);
	}

	public static String getLocaleCode(Player player) {
		return player.spigot().getLocale();
	}

	private static JavaPlugin getPlugin() {
		return JavaPlugin.getProvidingPlugin(LocaleConfig.class);
	}

	private static File getLangFolder() {
		return new File(getPlugin().getDataFolder(), "lang");
	}

	public static void setDefaults(Class<? extends Enum<? extends LocaleKey>> defaults) {
		LocaleConfig.defaults = new HashMap<>();
		for (Enum<? extends LocaleKey> constant : defaults.getEnumConstants()) {
			LocaleKey key = (LocaleKey) constant;
			String name = key.name().replace('_', '.').toLowerCase();
			LocaleConfig.defaults.put(name, key.get());
		}

		get(DEFAULT_CODE);
	}
}
