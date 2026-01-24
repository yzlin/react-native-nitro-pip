import type { HybridObject } from 'react-native-nitro-modules';

export interface AspectRatio {
  width: number;
  height: number;
}

export interface EnterPiPOptions {
  aspectRatio?: AspectRatio;
}

export type PiPModeChangedListener = (isInPiP: boolean) => void;

export interface NitroPip
  extends HybridObject<{ ios: 'swift'; android: 'kotlin' }> {
  enterPiP(options?: EnterPiPOptions): boolean;
  isPiPSupported(): boolean;
  isInPiP(): boolean;
  addPiPModeChangedListener(listener: PiPModeChangedListener): number;
  removePiPModeChangedListener(id: number): void;
}
