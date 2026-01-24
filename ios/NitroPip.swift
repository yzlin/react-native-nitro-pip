class NitroPip: HybridNitroPipSpec {
    public func enterPiP(options: EnterPiPOptions?) -> Bool {
        return false
    }

    public func isPiPSupported() -> Bool {
        return false
    }

    public func isInPiP() -> Bool {
        return false
    }
}
