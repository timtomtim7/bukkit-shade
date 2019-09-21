package blue.sparse.bshade.data.example;

import blue.sparse.bshade.data.attach.Attachment;
import blue.sparse.bshade.data.attach.PlayerAttachment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Examples extends JavaPlugin {

	@Override
	public void onEnable() {
		Attachment.add(MyPlayerData.class);

		Player player = null;
		MyPlayerData attachment = Attachment.get(player, MyPlayerData.class);
		System.out.println(attachment.distanceWalked);
	}

	public static class MyPlayerData extends PlayerAttachment {

		public double distanceWalked = 0.0;

		public MyPlayerData(Player player) {
			super(player);
		}

	}

}
