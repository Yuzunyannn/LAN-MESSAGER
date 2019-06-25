package client.frame.utility;

import javax.swing.JPanel;

import nbt.INBTSerializable;
import nbt.NBTTagCompound;

public abstract class JPanelUtility extends JPanel implements INBTSerializable<NBTTagCompound> {
	private static final long serialVersionUID = 1L;

	/** 首次创建 */
	public void firstCreate() {

	}
}

class JPanelUtilityBlank extends JPanelUtility {
	private static final long serialVersionUID = 1L;

	@Override
	public NBTTagCompound serializeNBT() {
		return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
	}

}