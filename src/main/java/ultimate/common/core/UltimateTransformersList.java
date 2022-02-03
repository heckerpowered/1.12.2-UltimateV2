package ultimate.common.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.spongepowered.asm.mixin.transformer.Proxy;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.ModAPITransformer;

public final class UltimateTransformersList extends ArrayList<IClassTransformer>
        implements Comparator<IClassTransformer> {
    public UltimateTransformersList() {
        super();
    }

    public UltimateTransformersList(Collection<IClassTransformer> c) {
        super(c);
    }

    @Override
    public boolean add(IClassTransformer e) {
        boolean result = super.add(e);
        sort(this);
        return result;
    }

    @Override
    public int compare(IClassTransformer o1, IClassTransformer o2) {
        if (ModAPITransformer.class == o1.getClass()) {
            return 1;
        }
        if (ModAPITransformer.class == o2.getClass()) {
            return -1;
        }

        if (o1 instanceof Proxy) {
            return 1;
        }
        if (o2 instanceof Proxy) {
            return -1;
        }

        return 0;
    }
}
