package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import javax.annotation.Nonnull;

public class DataEntryBlockPos extends DataEntryObject<BlockPos> {

	public DataEntryBlockPos(String name, @Nonnull BlockPos defaultValue, boolean isClientModificationAllowed) {
		super(name, defaultValue.immutable(), isClientModificationAllowed);
	}

	@Override
	public INBT write() {
		return DungeonGenUtils.writePosToList(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof ListNBT) {
			this.value = DungeonGenUtils.readPosFromList((ListNBT) nbt);
		}
	}

	@Override
	public void writeChanges(PacketBuffer buf) {
		buf.writeBlockPos(this.value);
	}

	@Override
	protected void readChangesInternal(PacketBuffer buf) {
		this.value = buf.readBlockPos();
	}

	@Override
	protected void setInternal(BlockPos value) {
		super.setInternal(value.immutable());
	}

}
