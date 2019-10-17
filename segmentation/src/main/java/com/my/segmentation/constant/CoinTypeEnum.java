package com.my.segmentation.constant;

/**
 * 币种枚举类
 *
 * @author: zhaozhonghui
 * @create: 2019-06-04 14:14
 */
public enum  CoinTypeEnum {
    BTC((byte)1,"比特币"),
    ETH((byte)2,"以太坊"),
    EOS((byte)3,"柚子"),
    QTHM((byte)4,"量子链"),
    ELF((byte)5,"ELF"),
    ;

    private Byte code;
    private String desc;

    CoinTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
