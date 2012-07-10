package xyz.mongo;

public interface IPageRequest {
	public static final int CNT_GET=1;
	public static final int CNT_NO_GET=0;
	/**
	 * 从一开始，第几页
	 * @return
	 */
    long getNumber();
    /**
     * 大于0，一页几条记录
     * @return
     */
    long getSize();
    /**
     * 是否要取总的记录数，一般第一次必取
     * 以后根据情况，可取可不去
     * 1，必须去
     * 0，不去
     * @return
     */
    int getCnt();
}
