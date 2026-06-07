package com.gestion.educativa.ui.carrera;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.CarreraRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CarreraViewModel_Factory implements Factory<CarreraViewModel> {
  private final Provider<CarreraRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public CarreraViewModel_Factory(Provider<CarreraRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public CarreraViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get());
  }

  public static CarreraViewModel_Factory create(Provider<CarreraRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new CarreraViewModel_Factory(repositoryProvider, tokenManagerProvider);
  }

  public static CarreraViewModel newInstance(CarreraRepository repository,
      TokenManager tokenManager) {
    return new CarreraViewModel(repository, tokenManager);
  }
}
