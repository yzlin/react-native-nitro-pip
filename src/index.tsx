import { NitroModules } from 'react-native-nitro-modules';

import type { NitroPip, PiPModeChangedListener } from './NitroPip.nitro';

export type { PiPModeChangedListener } from './NitroPip.nitro';

const NitroPipHybridObject =
  NitroModules.createHybridObject<NitroPip>('NitroPip');

export function enterPiP(options?: {
  aspectRatio?: { width: number; height: number };
}): boolean {
  return NitroPipHybridObject.enterPiP(options);
}

export function isPiPSupported(): boolean {
  return NitroPipHybridObject.isPiPSupported();
}

export function isInPiP(): boolean {
  return NitroPipHybridObject.isInPiP();
}

export function addPiPModeChangedListener(
  listener: PiPModeChangedListener
): () => void {
  const id = NitroPipHybridObject.addPiPModeChangedListener(listener);
  return () => {
    NitroPipHybridObject.removePiPModeChangedListener(id);
  };
}
