package com.github.elenterius.biomancy.integration.modonomicon;

import java.util.function.Consumer;

public final class ModonomiconIntegration {

	private ModonomiconIntegration() {}

	public static void init(Consumer<IModonomiconHelper> helperSetter) {
		helperSetter.accept(new ModonomiconHelperImpl());
	}

}
