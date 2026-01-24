class NitroPip: HybridNitroPipSpec {
    private var pipModeListenerId: Double = 0
    private var pipModeListeners: [Double: (Bool) -> Void] = [:]

    public func enterPip(options: EnterPipOptions?) -> Bool {
        return false
    }

    public func isPipSupported() -> Bool {
        return false
    }

    public func isInPip() -> Bool {
        return false
    }

    public func addPipModeChangedListener(listener: @escaping (Bool) -> Void) -> Double {
        let id = pipModeListenerId
        pipModeListenerId += 1
        pipModeListeners[id] = listener
        return id
    }

    public func removePipModeChangedListener(id: Double) {
        pipModeListeners.removeValue(forKey: id)
    }
}
