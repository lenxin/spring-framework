package test.aop;


/**
 * Simple interface to use for mixins
 *

 *
 */
public interface Lockable {

	void lock();

	void unlock();

	boolean locked();
}
