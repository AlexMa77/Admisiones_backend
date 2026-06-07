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
public final class CarreraRepository_Factory implements Factory<CarreraRepository> {
  private final Provider<ApiService> apiProvider;

  public CarreraRepository_Factory(Provider<ApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public CarreraRepository get() {
    return newInstance(apiProvider.get());
  }

  public static CarreraRepository_Factory create(Provider<ApiService> apiProvider) {
    return new CarreraRepository_Factory(apiProvider);
  }

  public static CarreraRepository newInstance(ApiService api) {
    return new CarreraRepository(api);
  }
}
