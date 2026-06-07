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
public final class EstudianteRepository_Factory implements Factory<EstudianteRepository> {
  private final Provider<ApiService> apiProvider;

  public EstudianteRepository_Factory(Provider<ApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public EstudianteRepository get() {
    return newInstance(apiProvider.get());
  }

  public static EstudianteRepository_Factory create(Provider<ApiService> apiProvider) {
    return new EstudianteRepository_Factory(apiProvider);
  }

  public static EstudianteRepository newInstance(ApiService api) {
    return new EstudianteRepository(api);
  }
}
