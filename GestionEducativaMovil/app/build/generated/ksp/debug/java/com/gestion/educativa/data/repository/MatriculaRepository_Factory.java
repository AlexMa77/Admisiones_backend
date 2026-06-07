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
public final class MatriculaRepository_Factory implements Factory<MatriculaRepository> {
  private final Provider<ApiService> apiProvider;

  public MatriculaRepository_Factory(Provider<ApiService> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public MatriculaRepository get() {
    return newInstance(apiProvider.get());
  }

  public static MatriculaRepository_Factory create(Provider<ApiService> apiProvider) {
    return new MatriculaRepository_Factory(apiProvider);
  }

  public static MatriculaRepository newInstance(ApiService api) {
    return new MatriculaRepository(api);
  }
}
