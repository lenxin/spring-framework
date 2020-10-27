package org.springframework.core.type.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * Type filter that is aware of traversing over hierarchy.
 * 可以遍历层次结构的类型筛选器。
 *
 * <p>This filter is useful when matching needs to be made based on potentially the
 * whole class/interface hierarchy. The algorithm employed uses a succeed-fast
 * strategy: if at any time a match is declared, no further processing is
 * carried out.
 * 当需要基于整个类/接口层次结构进行匹配时，此筛选器非常有用。
 * 所使用的算法使用一种快速成功的策略:如果在任何时候声明了一个匹配，则不执行进一步的处理。
 *
 * @since 2.5
 */
public abstract class AbstractTypeHierarchyTraversingFilter implements TypeFilter {
	protected final Log logger = LogFactory.getLog(getClass());
	//是否考虑继承关系
	private final boolean considerInherited;
	//是否考虑接口
	private final boolean considerInterfaces;

	protected AbstractTypeHierarchyTraversingFilter(boolean considerInherited, boolean considerInterfaces) {
		this.considerInherited = considerInherited;
		this.considerInterfaces = considerInterfaces;
	}

	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		// This method optimizes avoiding unnecessary creation of ClassReaders
		// as well as visiting over those readers.
		// 这种方法优化了避免不必要的类读取器的创建以及对这些读取器的访问。
		if (matchSelf(metadataReader)) {
			return true;
		}
		ClassMetadata metadata = metadataReader.getClassMetadata();
		if (matchClassName(metadata.getClassName())) {
			return true;
		}

		if (this.considerInherited) {
			String superClassName = metadata.getSuperClassName();
			if (superClassName != null) {
				// Optimization to avoid creating ClassReader for super class.
				//优化以避免为超类创建ClassReader
				Boolean superClassMatch = matchSuperClass(superClassName);
				if (superClassMatch != null) {
					if (superClassMatch.booleanValue()) {
						return true;
					}
				} else {
					// Need to read super class to determine a match...
					try {
						if (match(metadata.getSuperClassName(), metadataReaderFactory)) {
							return true;
						}
					} catch (IOException ex) {
						logger.debug("Could not read super class [" + metadata.getSuperClassName() +
								"] of type-filtered class [" + metadata.getClassName() + "]");
					}
				}
			}
		}

		if (this.considerInterfaces) {
			for (String ifc : metadata.getInterfaceNames()) {
				// Optimization to avoid creating ClassReader for super class
				//优化以避免为超类创建ClassReader
				Boolean interfaceMatch = matchInterface(ifc);
				if (interfaceMatch != null) {
					if (interfaceMatch.booleanValue()) {
						return true;
					}
				} else {
					// Need to read interface to determine a match...
					try {
						if (match(ifc, metadataReaderFactory)) {
							return true;
						}
					} catch (IOException ex) {
						logger.debug("Could not read interface [" + ifc + "] for type-filtered class [" +
								metadata.getClassName() + "]");
					}
				}
			}
		}

		return false;
	}

	private boolean match(String className, MetadataReaderFactory metadataReaderFactory) throws IOException {
		return match(metadataReaderFactory.getMetadataReader(className), metadataReaderFactory);
	}

	/**
	 * Override this to match self characteristics alone. Typically,
	 * the implementation will use a visitor to extract information
	 * to perform matching.
	 * 只匹配自己的特征。通常，该实现将使用访问者来提取信息以执行匹配。
	 */
	protected boolean matchSelf(MetadataReader metadataReader) {
		return false;
	}

	/**
	 * Override this to match on type name.
	 */
	protected boolean matchClassName(String className) {
		return false;
	}

	/**
	 * Override this to match on super type name.
	 */
	@Nullable
	protected Boolean matchSuperClass(String superClassName) {
		return null;
	}

	/**
	 * Override this to match on interface type name.
	 */
	@Nullable
	protected Boolean matchInterface(String interfaceName) {
		return null;
	}
}