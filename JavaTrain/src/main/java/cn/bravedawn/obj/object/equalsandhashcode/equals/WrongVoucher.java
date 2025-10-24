package cn.bravedawn.obj.object.equalsandhashcode.equals;

import java.util.Objects;

/**
 * PC: SPDB
 * author: fengx9
 * create time: 2022/9/26 13:04
 */
public class WrongVoucher extends Money{

    /**
     * 手动实现WrongVoucher的equals方法
     */

    private String store;

    public WrongVoucher(int amount, String currencyCode, String store) {
        super(amount, currencyCode);
        this.store = store;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (o == this)
//            return true;
//        if (!(o instanceof WrongVoucher))
//            return false;
//        WrongVoucher other = (WrongVoucher)o;
//        boolean currencyCodeEquals = (this.currencyCode == null && other.currencyCode == null)
//                || (this.currencyCode != null && this.currencyCode.equals(other.currencyCode));
//        boolean storeEquals = (this.store == null && other.store == null)
//                || (this.store != null && this.store.equals(other.store));
//        return this.amount == other.amount && currencyCodeEquals && storeEquals;
//    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WrongVoucher that = (WrongVoucher) o;
        return Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(store);
    }

    public static void main(String[] args) {
        Money cash = new Money(42, "USD");
        WrongVoucher voucher = new WrongVoucher(42, "USD", "Amazon");

        System.out.println(cash.equals(voucher)); // true

        /**
         * 按照上面equals方法的编写，我们乍一看是没有问题的，那这里为什么会返回false呢？
         * 原因就在于上面equals方法的  if (!(o instanceof WrongVoucher)) 这一句：
         *
         * 当一个子类 instanceof 父类时，返回true
         * 当一个父类 instanceof 子类是，返回false
         *
         * 所以上面的Money的实例 instanceof WrongVoucher时，也就是 父类 instanceof 子类，这里就会返回false
         * 很显然上面的equals方法违反了equals规定的对称性
         */
        System.out.println(voucher.equals(cash)); // false
    }


}
