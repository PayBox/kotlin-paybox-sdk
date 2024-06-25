package money.paybox.payboxsdk.config

enum class PaymentSystem {
    NONE,
    KAZPOSTKZT,
    CYBERPLATKZT,
    CONTACTKZT,
    SBERONLINEKZT,
    ONLINEBANK,
    CASHBYCODE,
    KASPIKZT,
    KAZPOSTYANDEX,
    SMARTBANKKZT,
    NURBANKKZT,
    BANKRBK24KZT,
    ALFACLICKKZT,
    FORTEBANKKZT,
    EPAYWEBKGS,
    EPAYKGS,
    HOMEBANKKZT,
    EPAYKZT,
    KASSA24,
    P2PKKB,
    EPAYWEBKZT,
    WAY4
}

enum class RequestMethod {
    GET, POST
}

enum class Language {
    ru, en, kz, de
}

enum class Region {
    DEFAULT, RU, UZ, KG
}
