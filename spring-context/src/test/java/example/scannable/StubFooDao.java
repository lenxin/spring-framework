package example.scannable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**

 */
@Repository
@Qualifier("testing")
public class StubFooDao implements FooDao {

	@Override
	public String findFoo(int id) {
		return "bar";
	}

}
