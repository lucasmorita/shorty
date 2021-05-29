# coding: utf-8
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "centos/7"

  # Executa a configuraçao do ansible
  config.vm.provision "ansible" do |ansible|
    ansible.verbose = "v"
    ansible.playbook = "config.yml"
  end

  config.vm.define "test" do |test_config|
    test_config.vm.hostname = "test"
    test_config.vm.network :private_network, :ip => "192.168.33.20"
  end

end
