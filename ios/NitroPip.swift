class NitroPip: HybridNitroPipSpec {
    private var pipModeListenerId: Double = 0
    private var pipModeListeners: [Double: (Bool) -> Void] = [:]

    public func enterPiP(options: EnterPiPOptions?) -> Bool {
        return false
    }

    public func isPiPSupported() -> Bool {
        return false
    }

    public func isInPiP() -> Bool {
        return false
    }

    public func addPiPModeChangedListener(listener: @escaping (Bool) -> Void) -> Double {
        let id = pipModeListenerId
        pipModeListenerId += 1
        pipModeListeners[id] = listener
        return id
    }

    public func removePiPModeChangedListener(id: Double) {
        pipModeListeners.removeValue(forKey: id)
    }
}
