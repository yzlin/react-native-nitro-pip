import { NitroModules } from 'react-native-nitro-modules';

import type { NitroPip, PipModeChangedListener } from './NitroPip.nitro';

export type { PipModeChangedListener } from './NitroPip.nitro';

const NitroPipHybridObject =
  NitroModules.createHybridObject<NitroPip>('NitroPip');

export function enterPip(options?: {
  aspectRatio?: { width: number; height: number };
}): boolean {
  return NitroPipHybridObject.enterPip(options);
}

export function isPipSupported(): boolean {
  return NitroPipHybridObject.isPipSupported();
}

export function isInPip(): boolean {
  return NitroPipHybridObject.isInPip();
}

export function addPipModeChangedListener(
  listener: PipModeChangedListener
): () => void {
  const id = NitroPipHybridObject.addPipModeChangedListener(listener);
  return () => {
    NitroPipHybridObject.removePipModeChangedListener(id);
  };
}
