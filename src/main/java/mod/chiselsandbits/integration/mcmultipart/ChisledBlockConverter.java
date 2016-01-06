package mod.chiselsandbits.integration.mcmultipart;

import java.util.Collection;
import java.util.Collections;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.IPartConverter;
import mcmultipart.multipart.IPartConverter.IReversePartConverter;
import mod.chiselsandbits.ChiselsAndBits;
import mod.chiselsandbits.chiseledblock.BlockChiseled;
import mod.chiselsandbits.chiseledblock.TileEntityBlockChiseled;
import mod.chiselsandbits.helpers.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

class ChisledBlockConverter implements IPartConverter, IReversePartConverter
{

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	@Override
	public Collection<Block> getConvertableBlocks()
	{
		return (Collection) ChiselsAndBits.getBlocks().getConversions().values();
	}

	@Override
	public Collection<? extends IMultipart> convertBlock(
			final IBlockAccess world,
			final BlockPos pos )
	{
		final TileEntity te = world.getTileEntity( pos );

		if ( te instanceof TileEntityBlockChiseled )
		{
			final ChisledBlockPart part = new ChisledBlockPart();

			part.bc = (BlockChiseled) world.getBlockState( pos ).getBlock();
			part.inner = new TileEntityBlockChiseled();
			part.inner.copyFrom( (TileEntityBlockChiseled) te );

			return Collections.singletonList( part );
		}

		return Collections.emptyList();
	}

	@Override
	public boolean convertToBlock(
			final IMultipartContainer container )
	{
		TileEntityBlockChiseled tile = null;

		for ( final IMultipart part : container.getParts() )
		{
			if ( part instanceof ChisledBlockPart )
			{
				tile = ( (ChisledBlockPart) part ).getTile();
			}
			else
			{
				return false;
			}
		}

		if ( tile != null )
		{
			tile.getWorld().setBlockState( tile.getPos(), tile.getPreferedBlock() );
			final TileEntityBlockChiseled te = ModUtil.getChiseledTileEntity( tile.getWorld(), tile.getPos(), true );

			if ( te != null )
			{
				te.copyFrom( tile );
			}

			return true;
		}

		return false;
	}

}
