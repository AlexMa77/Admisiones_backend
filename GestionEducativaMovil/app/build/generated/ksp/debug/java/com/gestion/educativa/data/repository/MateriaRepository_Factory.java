package com.gestion.educativa.data.repository;

import com.gestion.educativa.data.api.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class MateriaRepository_Factory implements Factory<MateriaRepository> {
  private final Provider<ApiService> apiProvider;

  public MateriaRepository_Factory(Provider<ApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public MateriaRepository get() {
    return newInstance(apiProvider.get());
  }

  public static MateriaRepository_Factory create(Provider<ApiService> apiProvider) {
    return new MateriaRepository_Factory(apiProvider);
  }

  public static MateriaRepository newInstance(ApiService api) {
    return new MateriaRepository(api);
  }
}
