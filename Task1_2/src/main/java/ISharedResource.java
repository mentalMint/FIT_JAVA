public interface ISharedResource {
    long read();

    void write() throws InterruptedException;
}
