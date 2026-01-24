import type { HybridObject } from 'react-native-nitro-modules';

export interface AspectRatio {
  width: number;
  height: number;
}

export interface EnterPipOptions {
  aspectRatio?: AspectRatio;
}

export type PipModeChangedListener = (isInPip: boolean) => void;

export interface NitroPip
  extends HybridObject<{ ios: 'swift'; android: 'kotlin' }> {
  enterPip(options?: EnterPipOptions): boolean;
  isPipSupported(): boolean;
  isInPip(): boolean;
  addPipModeChangedListener(listener: PipModeChangedListener): number;
  removePipModeChangedListener(id: number): void;
}
