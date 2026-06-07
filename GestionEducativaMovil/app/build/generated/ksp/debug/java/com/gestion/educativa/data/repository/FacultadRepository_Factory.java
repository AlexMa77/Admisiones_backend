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
public final class FacultadRepository_Factory implements Factory<FacultadRepository> {
  private final Provider<ApiService> apiProvider;

  public FacultadRepository_Factory(Provider<ApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public FacultadRepository get() {
    return newInstance(apiProvider.get());
  }

  public static FacultadRepository_Factory create(Provider<ApiService> apiProvider) {
    return new FacultadRepository_Factory(apiProvider);
  }

  public static FacultadRepository newInstance(ApiService api) {
    return new FacultadRepository(api);
  }
}
