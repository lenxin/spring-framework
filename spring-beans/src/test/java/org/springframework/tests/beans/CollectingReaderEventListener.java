package org.springframework.tests.beans;

import org.springframework.beans.factory.parsing.*;

import java.util.*;

public class CollectingReaderEventListener implements ReaderEventListener {
	private final List<DefaultsDefinition> defaults = new LinkedList<>();

	private final Map<String, ComponentDefinition> componentDefinitions = new LinkedHashMap<>(8);

	private final Map<String, List<AliasDefinition>> aliasMap = new LinkedHashMap<>(8);

	private final List<ImportDefinition> imports = new LinkedList<>();

	@Override
	public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
		this.defaults.add(defaultsDefinition);
	}

	public List<DefaultsDefinition> getDefaults() {
		return Collections.unmodifiableList(this.defaults);
	}

	@Override
	public void componentRegistered(ComponentDefinition componentDefinition) {
		this.componentDefinitions.put(componentDefinition.getName(), componentDefinition);
	}

	public ComponentDefinition getComponentDefinition(String name) {
		return this.componentDefinitions.get(name);
	}

	public ComponentDefinition[] getComponentDefinitions() {
		Collection<ComponentDefinition> collection = this.componentDefinitions.values();
		return collection.toArray(new ComponentDefinition[collection.size()]);
	}

	@Override
	public void aliasRegistered(AliasDefinition aliasDefinition) {
		List<AliasDefinition> aliases = this.aliasMap.get(aliasDefinition.getBeanName());
		if (aliases == null) {
			aliases = new ArrayList<>();
			this.aliasMap.put(aliasDefinition.getBeanName(), aliases);
		}
		aliases.add(aliasDefinition);
	}

	public List<AliasDefinition> getAliases(String beanName) {
		List<AliasDefinition> aliases = this.aliasMap.get(beanName);
		return (aliases != null ? Collections.unmodifiableList(aliases) : null);
	}

	@Override
	public void importProcessed(ImportDefinition importDefinition) {
		this.imports.add(importDefinition);
	}

	public List<ImportDefinition> getImports() {
		return Collections.unmodifiableList(this.imports);
	}
}