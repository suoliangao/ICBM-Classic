package icbm.zhapin.gui;

import icbm.api.ICBM;
import icbm.zhapin.ZhuYaoZhaPin;
import icbm.zhapin.jiqi.TXiaoFaSheQi;
import icbm.zhapin.rongqi.CXiaoFaSheQi;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GXiaoFaSheQi extends GuiContainer
{
	private TXiaoFaSheQi tileEntity;
	private GuiTextField textFieldX;
	private GuiTextField textFieldZ;
	private GuiTextField textFieldY;
	private GuiTextField textFieldFreq;

	private int containerWidth;
	private int containerHeight;

	public GXiaoFaSheQi(InventoryPlayer par1InventoryPlayer, TXiaoFaSheQi tileEntity)
	{
		super(new CXiaoFaSheQi(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.textFieldX = new GuiTextField(fontRenderer, 20, 21, 35, 12);
		this.textFieldY = new GuiTextField(fontRenderer, 20, 37, 35, 12);
		this.textFieldZ = new GuiTextField(fontRenderer, 20, 52, 35, 12);
		this.textFieldFreq = new GuiTextField(fontRenderer, 70, 33, 35, 12);
		this.textFieldFreq.setMaxStringLength(4);
		this.textFieldX.setMaxStringLength(6);
		this.textFieldZ.setMaxStringLength(6);
		this.textFieldY.setMaxStringLength(6);

		this.textFieldFreq.setText(this.tileEntity.getFrequency() + "");

		if (this.tileEntity.getTarget() == null)
		{
			this.textFieldX.setText(Math.round(this.tileEntity.xCoord) + "");
			this.textFieldZ.setText(Math.round(this.tileEntity.zCoord) + "");
			this.textFieldY.setText(Math.round(this.tileEntity.yCoord) + "");
		}
		else
		{
			this.textFieldX.setText(Math.round(this.tileEntity.getTarget().x) + "");
			this.textFieldZ.setText(Math.round(this.tileEntity.getTarget().z) + "");
			this.textFieldY.setText(Math.round(this.tileEntity.getTarget().y) + "");
		}
	}

	@Override
	public void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		this.textFieldX.textboxKeyTyped(par1, par2);
		this.textFieldZ.textboxKeyTyped(par1, par2);
		this.textFieldY.textboxKeyTyped(par1, par2);
		this.textFieldFreq.textboxKeyTyped(par1, par2);

		try
		{
			Vector3 newTarget = new Vector3(Integer.parseInt(this.textFieldX.getText()), Integer.parseInt(this.textFieldY.getText()), Integer.parseInt(this.textFieldZ.getText()));
			this.tileEntity.setTarget(newTarget);
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ZhuYaoZhaPin.CHANNEL, this.tileEntity, (int) 2, this.tileEntity.getTarget().x, this.tileEntity.getTarget().y, this.tileEntity.getTarget().z));
		}
		catch (NumberFormatException e)
		{
		}

		try
		{
			short newFrequency = (short) Math.max(Short.parseShort(this.textFieldFreq.getText()), 0);
			this.tileEntity.setFrequency(newFrequency);
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ZhuYaoZhaPin.CHANNEL, this.tileEntity, (int) 1, this.tileEntity.getFrequency()));
		}
		catch (NumberFormatException e)
		{
		}
	}

	/**
	 * Args: x, y, buttonClicked
	 */
	@Override
	public void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.textFieldX.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
		this.textFieldZ.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
		this.textFieldY.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
		this.textFieldFreq.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(tileEntity.getInvName(), 52, 6, 4210752);

		this.fontRenderer.drawString("X:", 8, 23, 4210752);
		this.fontRenderer.drawString("Y:", 8, 39, 4210752);
		this.fontRenderer.drawString("Z:", 8, 54, 4210752);

		this.fontRenderer.drawString("Frequency:", 70, 20, 4210752);

		this.textFieldX.drawTextBox();
		this.textFieldZ.drawTextBox();
		this.textFieldY.drawTextBox();
		this.textFieldFreq.drawTextBox();

		this.fontRenderer.drawString(this.tileEntity.getStatus(), 70, 50, 4210752);
		this.fontRenderer.drawString(this.tileEntity.getVoltage() + "v", 70, 60, 4210752);
		this.fontRenderer.drawString(ElectricInfo.getDisplayShort(this.tileEntity.getJoules(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplayShort(this.tileEntity.getMaxJoules(), ElectricUnit.JOULES), 70, 70, 4210752);

		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture(ICBM.TEXTURE_FILE_PATH + "gui_cruise_launcher.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (!this.textFieldX.isFocused())
			this.textFieldX.setText(Math.round(this.tileEntity.getTarget().x) + "");
		if (!this.textFieldZ.isFocused())
			this.textFieldZ.setText(Math.round(this.tileEntity.getTarget().z) + "");
		if (!this.textFieldY.isFocused())
			this.textFieldY.setText(Math.round(this.tileEntity.getTarget().y) + "");
		if (!this.textFieldFreq.isFocused())
			this.textFieldFreq.setText(this.tileEntity.getFrequency() + "");
	}
}