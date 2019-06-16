package resmgt;

import java.awt.Image;

import javax.swing.ImageIcon;

public class UserResource {

	public static enum HeadIconSize {
		BIG(80, "big"), SMALL(40, "small");

		final public int size;
		final public String str;

		HeadIconSize(int size, String s) {
			this.size = size;
			this.str = s;
		}

		@Override
		public String toString() {
			return this.str;
		}
	}

	static {
		ResourceInfo info = ResourceManagement.instance.getResource("img/debug_headicon.png");
		info = ResourceManagement.instance.loadTmpResource(info, "headicon/debug_big");
		info.setImage(
				info.getImage().getScaledInstance(HeadIconSize.BIG.size, HeadIconSize.BIG.size, Image.SCALE_DEFAULT));
		info = ResourceManagement.instance.loadTmpResource(info, "headicon/debug_small");
		info.setImage(info.getImage().getScaledInstance(HeadIconSize.SMALL.size, HeadIconSize.SMALL.size,
				Image.SCALE_DEFAULT));
	}

	/** 获取用户头像 */
	static public ImageIcon getHeadIcon(String username, HeadIconSize size) {
		return new ImageIcon(ResourceManagement.instance.getTmpResource("headicon/debug_" + size).getImage());
	}

}
